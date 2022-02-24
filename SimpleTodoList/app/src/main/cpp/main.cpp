//
//  main.cpp
//  DailyQuote
//

#include <iostream>
#include "daily_quote.hpp"

int main(int argc, const char * argv[]) {
    auto dailyQuote = DailyQuote();
    std::cout << dailyQuote.Get() << std::endl;
    return 0;
}
