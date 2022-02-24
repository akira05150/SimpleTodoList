cmake_minimum_required(VERSION 3.4.1)

project("simpletodolist")
add_library( # Sets the name of the library.
             daily_quote

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             daily_quote.cpp)

find_library( # Sets the name of the path variable.
              daily_quote

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log)

target_link_libraries( # Specifies the target library.
                     daily_quote

                     # Links the target library to the log library
                     # included in the NDK.
                     ${log-lib})
