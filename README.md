=======================================================================================
Antigen: Large-scale individual-based simulation of pathogen epidemiology and evolution
=======================================================================================

Antigen implements an SIR epidemiological model where hosts in a population are infected with
viruses that have distinct antigenic phenotypes.  Hosts make contacts trasmitting viruses and
also recover from infection.  After recovery, a host remembers the antigenic phenotype it was
infected with as part of its immune history.  The risk of infection after contact depends on 
comparing the infecting virus's phenotype to the phenotypes in the host immune history.

Antigenic phenotype is currently implemented as a n-d continuous vector.  Virus mutations move 
phenotype randomly in this n-d Euclidean space.  However, other phenotypes may be implemented
through the Phenotype interface.

Additionally, population structure is implemented as discrete demes.  Contacts within a deme occur
through standard mass action, while contacts between demes occur at some fraction of the rate of
within deme contact.

-------------------------------------------

Copyright Trevor Bedford 2010-2012. Distributed under the GPL v3.