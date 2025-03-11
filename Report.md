# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.
   
`==` is for Reference Comparison, it checks if two references point to the same memory location.
`.equals()` is for Value Comparison it is used to compare actual values of objects, rather than memory locations.


```java
   public class StringComparison {
   public static void main(String[] args) {
      String s1 = "Hello";
      String s2 = new String("Hello");
      
      // == checks reference
      System.out.println(s1 == s2); // false (different objects in memory)

      // .equals() checks value
      System.out.println(s1.equals(s3)); // true (content is the same)
   }
}
   
   ```


2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner? 

"apple" would come before "Banana" since the ASCII value for letter "a" is greater than "B". To sort a list of string in case-insensitive manner, we can use `String.CASE_INSENSITIVE_ORDER`,
which is a built-in Java Comparator that ignores case when sorting strings.


3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
    Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point. 

The order of these checks matters because of how string matching works when using contains(). Since ">=" contains ">", and "<=" contains "<", 
checking for ">" before ">=" (or "<" before "<=") would lead to incorrect behavior.

Consider the following example, if we check for ">" first, the method returns Operations.GREATER_THAN instead of Operations.GREATER_THAN_EQUALS.
This is wrong since `rating>=8` should mean "greater than or equal to," not just "greater than."

```java
String condition = "rating>=8";
Operations op = getOperatorFromStr(condition);
System.out.println(op);

public static Operations getOperatorFromStr(String str) {
   if (str.contains(">")) { 
      return Operations.GREATER_THAN;
   } else if (str.contains(">=")) {
      return Operations.GREATER_THAN_EQUALS;
   }
}
```


4. What is the difference between a List and a Set in Java? When would you use one over the other?
  
A `List` maintains the insertion order of elements, allows duplicate elements, and provides indexed access to elements. 
A `Set` does not allow duplicate elements, does not guarantee order, and provides fast lookup and ensures uniqueness.

We use `List` when we have duplicates, need indexed access, or when order matters. While if we need unique elements and do not care about order, we should use `Set`.


5. In [GamesLoader.java](src/main/java/student/GamesLoader.java), we use a Map to help figure out the columns. What is a map? Why would we use a Map here? 

`Map` is a collection that associates keys with values, it stores key-value pairs.

In `GamesLoader.java`, a `Map` is used to dynamically map CSV column names to their respective indexes.


6. [GameData.java](src/main/java/student/GameData.java) is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?

`enum` is a fixed set of constants, the values are predefined and immutable.

In `GameData.java`, the `enum` represents column names from the CSV file, and ensure that  the columns in the CSV file is finite and immutable.





7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ```

   ```java
   if (ct == ConsoleText.CMD_QUESTION || ct == ConsoleText.CMD_HELP) {
      processHelp();
   } else if (ct == ConsoleText.INVALID) {
           CONSOLE.printf("%s%n", ConsoleText.INVALID);
   } else {
           CONSOLE.printf("%s%n", ConsoleText.INVALID);
   }
   
   ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block. 

```text
*******欢迎来到 BoardGame Arena 规划工具*******

这是一个帮助玩家规划在 Board Game Arena 上游玩的游戏的工具。

要开始，请在下方输入您的第一个命令，或者输入 ? 或 help 来查看可用的命令选项。

> help
要使用 BGArenaPlanner，您可以筛选 BGA 游戏列表、添加游戏到您的列表、从列表中移除游戏，并将您的游戏列表保存到文件中。
筛选是渐进式的，这意味着您可以添加多个筛选条件来缩小列表范围。

可用命令如下：
- `exit` - 退出程序
- `help` 或 `? [list | filter]` - 显示帮助信息  
  - `list` - 显示 `list` 命令的帮助信息  
  - `filter` - 显示 `filter` 命令的帮助信息  

> filter name~=Catan
未指定筛选条件。当前筛选列表如下：

> exit
再见！祝您游戏愉快！
```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?

As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry.

Localization is crucial in software development as it enables products to cater to diverse linguistic and cultural audiences, 
thereby enhancing user experience and expanding market reach.

However, improper localization can lead to misunderstandings and damage a brand's reputation. For instance, 
the lack of official support often leads communities to develop unofficial patches to address localization needs, 
which may not always align with the original software's intent. To mitigate such risks, developers should gain a deep 
understanding of the target market's cultural context, ensure accurate translations, and consider localization requirements 
during the design phase. This proactive approach helps prevent potential pitfalls and ensures that the software resonates well 
with users across different regions.

As a developer, you can minimize issues when expanding a program to other languages by separating text from code using 
resource files, making translations easier to manage. Using Unicode (UTF-8) encoding ensures proper character display across 
different languages. Additionally, designing a flexible UI that accommodates text expansion prevents layout issues in languages 
with longer words or phrases. Implementing locale-aware formatting for dates, numbers, and currencies helps adapt to regional 
differences. Finally, working with native speakers or professional translators ensures accuracy, and thorough testing in multiple 
languages helps catch potential problems before release.