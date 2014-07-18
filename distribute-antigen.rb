#!/usr/bin/ruby

replicates = ["8"]
mixings = ["diff", "eq"]
mus =["0.00005", "0.00015", "0.00025", "0.00035", "0.00045", "0.00055", "0.00065"]

replicates.each { |replicate|
	mixings.each { |mixing|
		mus.each { |mu|
	
			stem = "antigen_mu_" + mu + "_" + mixing + "_" + replicate
			Dir.chdir(stem)
			`sbatch -n 1 -c 4 -t 24:00:00 --mem=24000 ../antigen.sh`
			Dir.chdir("..")
	
		}
	}
}
