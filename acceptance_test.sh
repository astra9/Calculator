#!/bin/bash

test $(curl localhost:7777/sum?a=1\&b=2) -eq 3