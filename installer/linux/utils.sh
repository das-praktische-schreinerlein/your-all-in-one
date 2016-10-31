#!/usr/bin/env bash
###################
## utils
###################

function replaceConfigFileBySed {
   echo "# start replaceConfigFileBySed $1 $2"
   cat $1 | sed -r -f $2 > $1-new
   diff $1 $1-new
   mv $1 $1-dist
   mv $1-new $1
}

export replaceConfigFileBySed
