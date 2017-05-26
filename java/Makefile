NAME:=SimpleComm
SOURCES:=$(wildcard com/industrialshields/simplecomm/*.java)
CLASSES:=$(patsubst %.java,%.class,$(SOURCES))
TESTS:=$(wildcard test/*.java)
TESTS_CLASSES:=$(patsubst %.java,%.class,$(TESTS))
JAR:=$(NAME).jar
DOC_CONFIG:=doxygen.config

.PHONY: first all world
first all world: $(JAR)

$(CLASSES): $(SOURCES)
	javac $^

$(JAR): $(CLASSES)
	jar cvf $(@) $^

.PHONY: doc
doc: $(SOURCES)
	doxygen $(DOC_CONFIG)

.PHONY: tests
tests: $(TESTS_CLASSES)

$(TESTS_CLASSES): $(TESTS) $(SOURCES)
	javac $^

.PHONY: clean
clean:
	rm -f $(JAR) $(CLASSES) $(TESTS_CLASSES)
