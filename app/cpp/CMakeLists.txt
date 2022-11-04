cmake_minimum_required(VERSION 3.10.2)

project("main")
add_library(
        main
        SHARED
        main.cpp
        argon2/blake2/blake2.c
        argon2/argon2.c
        argon2/argon2-arch.c
        argon2/core.c
        argon2/encoding.c
        argon2/genkat.c
        argon2/impl-select.c
        argon2/thread.c
)

find_library(log-lib log)
target_link_libraries(main ${log-lib})