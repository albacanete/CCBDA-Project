{
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

      # providers.fbprophet = ["conda"];

      # Specify python requirements, you can use ./requirements.txt a
      # string (or a combination of both)
      requirements = ''
        ipython
        black
        python-lsp-server
        python-language-server[all]
      ''
        + (builtins.readFile ./requirements.txt);
    };

  in
  {
    devShells.default = with pkgs; mkShellNoCC {
      name = "python-analysis";
      buildInputs =  [
        python-env
        jq
        csvkit
      ];
    };
  });
}
