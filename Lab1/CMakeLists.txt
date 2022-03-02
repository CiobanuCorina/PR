cmake_minimum_required(VERSION 3.21)
project(untitled C)

set(CMAKE_C_STANDARD 99)# see https://cmake.org/cmake/help/latest/module/FindBoost.html

include_directories(D:/Cora/univer/SemVII/PR/pcre2)
# Note: a target should be already defined using 'add_executable' or 'add_library'


link_libraries(ws2_32 wsock32)

add_executable(untitled main.c)
