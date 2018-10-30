#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import argparse
import datetime
import json
import os
import pprint
import sys

import requests

################################################################################################
# TODO:
################################################################################################


################################################################################################
# Constants
#
################################################################################################
URL_QUOTE_SERVER = "http://localhost:8080/api/v1/quotes"
DEFAULT_LANG = "korean"


################################################################################################
# Functions
#
################################################################################################
def parse_quote_file(filename, tags, lang=DEFAULT_LANG):
    print(filename)
    result = []
    with open(filename) as f:
        for line in f:
            if len(line.strip()) != 0 and not line.startswith("#"):
                lines = line.split("-")
                result.append({
                    "quote": lines[0].strip(),
                    "author": lines[1].strip(),
                    "created": datetime.datetime.today().strftime('%Y-%m-%d'),
                    "tags": tags,
                    "lang": lang
                })
    return result


def send_one_quote(url, quote):
    pprint.pprint(quote)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    r = requests.post(url, data=json.dumps(quote), headers=headers)
    print("r", r)


def send_bulk_quotes(url, quote_list):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    r = requests.post(url, data=json.dumps(quote_list), headers=headers)
    print("r", r)


################################################################################################
# Main function
#
################################################################################################

def main():
    parser = argparse.ArgumentParser(description="Uploading quotes to API server: " + URL_QUOTE_SERVER)

    parser.add_argument("-t", "--tags", nargs='+',
                        help="sending quotes to " + URL_QUOTE_SERVER, required=True)
    parser.add_argument("-f", "--file", action="store", dest="filename", help="input file for quote", required=True)

    args = parser.parse_args()
    print(args)

    if args.filename:
        print("uploading...")
        if os.path.exists(args.filename):
            send_bulk_quotes(URL_QUOTE_SERVER, parse_quote_file(args.filename, args.tags))

            # test
            # send_one_quote(URL_QUOTE_SERVER, parse_quote_file(args.filename, args.tags)[0])
        else:
            print("filename not found: " + args.filename)


if __name__ == "__main__":
    sys.exit(main())
