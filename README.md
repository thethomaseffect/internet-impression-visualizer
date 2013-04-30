Internet Impression Visualizer
==============================

# Introduction

This application takes a bing search term and attempts to build a visual word cloud from search results and from those result's children. It does this by attempting to score the links and word on the page.

## Disclaimer

This application was developed for academic purposes and should not in any circumstance be used in a production environment. The crawler is completely ignorant of files like robots.txt meaning that it does not respect the wises of site owners. That said, if you would like to submit a pull request adding this functionality I would be delighted to accept.

# Before Compiling

You will need to add your Bing API key encoded in base64 to \src\ie\gmit\impressionengine\searchparser\BingSearch.java in the format:

\<Customer API Key\>:\<Customer API Key\>

You will need to get an API key free from Bing [here](https://datamarket.azure.com/dataset/5BA839F1-12CE-4CCE-BF57-A49D98D29A44) and you can use a free online base64 encoder like [this one](http://www.opinionatedgeek.com/dotnet/tools/base64decode/). Where everything is done it should look something like this:

```java
private static final String ENCODED_API_KEY = "f4SDf34rweFER38jfsdsdfsdfs"
			+ "49jfFSDF4fwj0lbmfdgfdfsf89efsdlfkoiw984efddkf"
			+ "FGJDJS4884dfghsdhf339jdsfFHEGSDFGSDG343304fs=";
```

# Building

Add the jar files in /lib to your classpath and build a runnable jar. Until I add a ANT script, eclipse is probably the easiest way to do this.

# Usage

If everything is working you can run the application using:

```shell
java -jar crawler.jar <YOUR SEARCH TERM>
```

You should see output from the applications various components. As soon as 100 high-scoring words are found the search will terminate and a swing window will open with the cloud.

# Internal Workings

The heart of the program are three Runnable classes that execute the classic producer-consumer pattern.

 * The Crawler gets a link form the link queue, Downloads a web page and adds it to the page queue.
 * The Scorer gets a page from the page queue, scores the links and adds them to the links queue, scores the words and adds them to the cloud words queue.
 * The Cloud Builder takes words from the cloud words queue and adds them to a word cloud object.

This is possible by sharing access to data structures optimized for thread-safe concurrent usage. All of the consumers are designed to await data when their respective work queue is empty.

The runnables are contained in thread pools and can be safely shutdown by setting a static volatile boolean shutdown switch to true. The Executor service will wait until all threads have safely shutdown before processing the final word cloud.

The program is designed for excellent time and space efficiency. By the use of a comparator for the link queue and avoiding state where possible the only constant in memory is the word cloud, which utilizes a MultiSet so each word is actually only stored once. Thanks to the excellent methods provided by the Java concurrent collection, threads waste no time with sleep cycles when there is no data available.

Links are scored based on:
 * If a link with the same host-name was previously visited (Negative)
 * If the link contains any or all parts of the search term (Positive)
 * If the hyper-linked text contains any or all parts of the search term (Positive)

An additional rule I didn't get time to implement was:
 * For each link text, get the index of it in the words-only page. Then compare these indexes to the indexes of the search term to determine if the link is near an occurrence of the search term. (Positive)

Words are scored based on:
 * (Closeness to the search term) * (Is the word an adjective). This means adjectives near the search term score the highest, while other words can still achieve reasonable scores. (Positive)
 * Is a stop word (Not included). Implemented as a rule for uniformity.

Rules ideally implement the iRule interface, which utilizes generics to allow rules of any one type. This is provided more to enforce the notion of what a rule is and to give it a certain degree of predictability. All rules return a floating point value between 0 and 1 and are then multiplied by scaler values to allow easy tweaking of the scoring algorithms. Literal numbers can then be added to the result if the rule is always deemed to have some value. A good example of this is a word close to the search term that is not an adjective.

The search terms is provided by using command-line arguments. If none are present the program prints a help message with usage instructions. The search uses the official Bing API with results in the JSON format. The application processes these results using the Jackson Parser with is much more suited to RESTful APIs than Google GSON. The links are then added to a list which is added to the links queue.

The connection uses basic security, sending the API key encoded in base64. Since I wanted as few dependencies as possible in the project, I used an on-line base64 encoder to do this myself and saved it as a literal string inside the application. The bing search is configured to not return any adult links so the application is safe for children. Unfortunately, I didn't get time to handle this, so if certain search terms such as "Free Porn" are entered the program hangs. I nether wanted to create a static collection of "dirty" words to prevent this nor did I want to disable the safe-search so I left it as it is.

The problem with designing for maximum scalability is that hard-coding stopping conditions into the code increases risk of error and coupling. The program can quite happily work away crawling hundreds of pages but there are cases where:

 * All the words returned for a search term are the same (ie. the same 30 or so keep cropping up).
 * None of the links score sufficiently high enough to be considered candidates for the links queue.
For large word targets (ie. 100000) this can cause the program to run out of links and hang. The code to handle these cases would certainly be worthy of it's own class. My proposed solution would be to detect when no new words have been added for a substantial amount of time, then examine the state of the links and page queue. This class could then potentially inject results 11 - 20 of the bing search into the links queue to get things moving again, or give up and form a cloud out of what's been found.

# Licence

Licenced under the MIT licence.

The MIT License (MIT)

Copyright (c) 2013 Thomas Geraghty

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.