NAME:=SimpleComm
SOURCES:=$(wildcard com/industrialshields/simplecomm/*.java)
CLASSES:=$(patsubst %.java,%.class,$(SOURCES))
TESTS:=$(wildcard test/*.java)
TESTS_CLASSES:=$(patsubst %.java,%.class,$(TESTS))
JAR:=$(NAME).jar
DOC_CONFIG:=doxygen.config
DOC_DIR:=doc

.PHONY: first all world
first all world: $(JAR)

$(CLASSES): $(SOURCES)
	javac $^

$(JAR): $(CLASSES)
	jar cvf $(@) $^

.PHONY: doc doc-clean
doc: $(SOURCES)
	doxygen $(DOC_CONFIG)

doc-clean:
	rm -rf $(DOC_DIR)

.PHONY: tests tests-clean
tests: $(TESTS_CLASSES)

tests-clean:
	rm -f $(TESTS_CLASSES)

$(TESTS_CLASSES): $(TESTS) $(SOURCES)
	javac $^

.PHONY: clean
clean: doc-clean tests-clean
	rm -f $(JAR) $(CLASSES)
