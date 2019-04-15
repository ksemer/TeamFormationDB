f = open("skills.txt", "r")
w = open("stats.txt", "w")
lines = f.readlines()

for line in lines:
	skill,users = line.split("\t")
	w.write(skill + "\t" + str(len(users.split(","))) + "\n")