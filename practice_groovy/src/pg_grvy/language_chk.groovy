/**
 * Programming Groovy
 */
sep = {println "------------------------"}
// データ型
def foo = 3
def bar() {}

// GString
def str = """Hello,
World
."""

println "Reply> ${str}"

sep()
// "", //
a = "a\nbc"
b = /a\nbc/

println a
println b
sep()

// Closure
clos1 = {          println "Hello closure!"}
clos2 = {target -> println "Hello ${target}!"}
clos3 = {          println "Hello $it!"}
clos4 = {       -> println "Hello Not Args!"}
clos5 = {String arg, int i -> println "Hello $arg, $i!"}

clos1()
clos2("args")
clos3("args it")
clos4()
clos5("args it", 100)
sep()

(1..10).each{print it}
println ""
(1..<10).each{print it}
println ""
5.times{print "five!"}
println ""

sep()

for(int i: 1..3)println "for $i";
sep()