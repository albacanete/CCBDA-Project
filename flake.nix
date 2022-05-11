{
  description = "Python environment managed with mach-nix and flakes";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";

    flake-utils = {
      url = "github:numtide/flake-utils";
      inputs.nixpkgs.follows = "nixpkgs";
    };

    # use `nix flake lock --update-input pypi-deps-db` to update the pypi database
    # or `nix flake update` to update all
    pypi-deps-db = {
      url = "github:DavHau/pypi-deps-db";
      inputs.nixpkgs.follows = "nixpkgs";
      inputs.mach-nix.follows = "mach-nix";
    };

    mach-nix = {
      url = "github:bjornfor/mach-nix/adapt-to-make-binary-wrapper";
      # TODO: Change back to upstream once this is merged:
      # - https://github.com/DavHau/mach-nix/pull/445
      # url = "github:DavHau/mach-nix";
      inputs.nixpkgs.follows = "nixpkgs";
      inputs.flake-utils.follows = "flake-utils";
      inputs.pypi-deps-db.follows = "pypi-deps-db";
    };

  };
  outputs = { self, nixpkgs, flake-utils, mach-nix, ...}:

  flake-utils.lib.eachDefaultSystem (system:
  let
    pkgs = import nixpkgs { inherit system; };

    # Do NOT use import mach-nix {inherit system;};
    #
    # otherwise mach-nix will not use flakes and pypi-deps-db
    # input will not be used:
    # https://github.com/DavHau/mach-nix/issues/269#issuecomment-841824763
    mach = mach-nix.lib.${system};

    python-env = mach.mkPython {
      # Choose python version
      python = "python38";

      # Specify python requirements, you can use ./requirements.txt a
      # string (or a combination of both)
      requirements = ''
        tzdata
        ipython
        black
        python-lsp-server
        python-language-server[all]
      ''
        + (builtins.readFile ./backend/requirements.txt)
        + (builtins.readFile ./scrapy/requirements.txt);
    };

  in
  {
    devShells.default = with pkgs; mkShellNoCC {
      name = "python";
      buildInputs =  [
        python-env
        gnupg
        openssh
        jq
        (writeShellScriptBin "aws" ''
          unset PYTHONPATH
          exec ${pkgs.awscli2}/bin/aws "$@"
        '')
        (writeShellScriptBin "ssh-aws" ''
          ADDR="$1"
          shift
          exec TERM=xterm ${pkgs.openssh}/bin/ssh ec2-user@"$ADDR" "$@"
        '')
      ];

      shellHook = let
        GIT_HOOKS = ( symlinkJoin { name = "git-hooks"; paths = [
          (writeShellScriptBin "pre-commit" ''
            ${gitleaks}/bin/gitleaks protect --verbose --redact --staged
          '')
        ]; } ) + "/bin";
      in "git config --local core.hooksPath ${GIT_HOOKS}";
    };
    packages.default = self.packages.${system}.backend;
    packages.backend = let backend = ./backend; in pkgs.writeShellScriptBin "manage"
    ''
      export PYTHONPATH=${python-env}/bin;
      export PATH=${python-env}/bin:$PATH;

      python ${backend}/manage.py "$@"
    '';

    packages.dockerImage = pkgs.dockerTools.buildLayeredImage {
      name = "backend";
      tag = "latest";
      contents = [ self.packages.${system}.backend ];
      config = {
        Cmd = [ "manage" "runserver" ];
        ExposedPorts = {
          "8000/tcp" = {};
        };
      };
    };

  });
}
