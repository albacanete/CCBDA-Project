#!/usr/bin/env bash

# Splits data into players and teams (squads)
# and saves them in two separate CSV files (players.csv and teams.csv)
#
# Usage: ./split_data.sh [<data_file>] [<output_dir>]
#
# If SAVE_INTERMEDIATE is set, the intermediate json files
# will be saved in the output directory.

INPUT=${1:-../scrapy/transfermarkt.json}
OUTPUT_FOLDER=${2:-.}

function save_intermediate_json() {
    # If SAVE_INTERMEDIATE is defined, save intermediate json files
    if [ ! -z "$SAVE_INTERMEDIATE" ]; then
        echo "Saving JSON $@" >&2
        tee "$@"
    else # Otherwise just pipe them through
        cat
    fi
}


echo "Processing $INPUT to players..." >&2
cat "$INPUT" | jq '[ .[] | select(.name_player != null) ]' | \
    save_intermediate_json "${OUTPUT_FOLDER}/players.json" | \
    in2csv -f json > "${OUTPUT_FOLDER}/players.csv"

echo "Processing $INPUT to squads..." >&2
cat "$INPUT" | jq '[ .[] | select(.name_player == null) ]' | \
    save_intermediate_json "${OUTPUT_FOLDER}/squads.json" | \
    in2csv -f json > "${OUTPUT_FOLDER}/squads.csv"
