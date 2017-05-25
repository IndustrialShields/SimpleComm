NAME:=SimpleComm
SOURCES:=$(wildcard com/industrialshields/simplecomm/*.java)
CLASSES:=$(patsubst %.java,%.class,$(SOURCES))
TESTS:=$(wildcard test/*.java)
TESTS_CLASSES:=$(patsubst %.java,%.class,$(TESTS))
JAR:=$(NAME).jar

.PHONY: first all world
first all world: $(JAR)

$(CLASSES): $(SOURCES)
	javac $^

$(JAR): $(CLASSES)
	jar cvf $(@) $^

.PHONY: tests
tests: $(TESTS_CLASSES)

$(TESTS_CLASSES): $(TESTS) $(SOURCES)
	javac $^

.PHONY: clean
clean:
	rm -f $(JAR) $(CLASSES) $(TESTS_CLASSES)
