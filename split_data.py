import os
import random
import re
import shutil
import numpy.random as random

def main():
	data_dirs = ['training', 'dev', 'testing']
	data_dir_probs = [0.6, 0.2, 0.2]
	# remove files in currently existing directories for a clean training set
	for data_dir in data_dirs:
		shutil.rmtree('%s/', data_dir)

	puzzle_length_dirs = os.listdir('all-puzzles/')
	for puzzle_length_dir in filter(lambda s: s.isdigit(), puzzle_length_dirs):
		# only copy puzzle files
		r = re.compile("\d+\.txt")
		for puzzle_file in filter(r.match, os.listdir('all-puzzles/%s/' % puzzle_length_dir)):
			data_dir = random.choice(data_dirs, p=data_dir_probs)
			old_path = 'all-puzzles/%s/%s' % (puzzle_length_dir, puzzle_file)
			new_dir = 'all-puzzles/%s/%s/' % (data_dir, puzzle_length_dir)
			# make the directory if it doesn't exist
			if not os.path.exists(new_dir):
				os.makedirs(new_dir)
			# copy over the file
			shutil.copy(old_path, new_dir)

if __name__ == '__main__':
	main()
