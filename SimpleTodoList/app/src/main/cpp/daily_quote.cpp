//
//  daily_quote.cpp
//  DailyQuote
//

#include "daily_quote.hpp"
#include <random>
#include <array>
#include <jni.h>

using namespace std;

string DailyQuote::Get() const {
    array<string, 3> quotes{ {"Stay Hungry, Stay Foolish.", "Cleverness is a gift, kindness is a choice.", "Always deliver more than expected."} };
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dist(0, quotes.size() - 1);
    return quotes[dist(gen)];
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_simpletodolist_MainActivity_getQuote(
        JNIEnv* env,
        jobject /* this */) {
    auto dailyQuote = DailyQuote();
    std::string quote = dailyQuote.Get();
    return env->NewStringUTF(quote.c_str());
}
