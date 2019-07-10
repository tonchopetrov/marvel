#!/bin/bash
echo "Enter path to project"
read var
sh ./build_translation.sh $var

sh ./build_heroes.sh $var