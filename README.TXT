This datasource has no direct dependencies on the rest
of the Metadata codebase as it is a thin client to the 
structure service implementing a Methode datasource.

It should be integration tested whenever the structure 
service implementation is modified. No XML validation
is used on the API responses so XSD changes will not
necessarily break the response parsing.