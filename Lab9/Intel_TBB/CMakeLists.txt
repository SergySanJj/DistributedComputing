cmake_minimum_required(VERSION 3.13)
project(Intel_TBB)

set(CMAKE_CXX_STANDARD 14)

add_executable(Intel_TBB main.cpp)

include_directories(C:/work/tbb/include)
target_link_libraries(Intel_TBB C:/work/tbb/lib/intel64/vc14/tbb.lib C:/work/tbb/lib/intel64/vc14/tbb_debug.lib)