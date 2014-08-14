#!/bin/bash

echo "${0%/*}"
cd "${0%/*}"

rm -rf ./Kaboha.iconset
mkdir ./Kaboha.iconset

cp Kaboha.png Kaboha.iconset/icon_128x128@2x.png
cp Kaboha.png Kaboha.iconset/icon_256x256.png

sips -z 16 16     ./Kaboha.png --out ./Kaboha.iconset/icon_16x16.png
sips -z 32 32     ./Kaboha.png --out ./Kaboha.iconset/icon_16x16@2x.png
sips -z 32 32     ./Kaboha.png --out ./Kaboha.iconset/icon_32x32.png
sips -z 64 64     ./Kaboha.png --out ./Kaboha.iconset/icon_32x32@2x.png
sips -z 128 128   ./Kaboha.png --out ./Kaboha.iconset/icon_128x128.png
sips -z 512 512   ./Kaboha.png --out ./Kaboha.iconset/icon_256x256@2x.png
sips -z 512 512   ./Kaboha.png --out ./Kaboha.iconset/icon_512x512.png
sips -z 1024 1024   ./Kaboha.png --out ./Kaboha.iconset/icon_512x512@2x.png

iconutil -c icns -o ./Kaboha.icns ./Kaboha.iconset

rm -rf Kaboha.iconset