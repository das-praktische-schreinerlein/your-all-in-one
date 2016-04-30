#!/bin/bash
# create init-basenode.sql

# load utils
source utils.bash

# create tpl
cat init-basenode-src.sql | sed -r -f init-basenode-sql.sed > init-basenode.sql.tmp
cp init_basenode-tpl.sql init_basenode.sql
replace_text_with_file init_basenode.sql XXXSQLXXX init-basenode.sql.tmp
rm init-basenode.sql.tmp