#!/bin/bash

test $(curl 192.168.1.55:7777/sum?a=1\&b=2) -eq 3