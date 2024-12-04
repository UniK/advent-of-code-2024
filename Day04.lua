-- Configurations
local Config = {
	SUB_GRID_SIZE = 3,
	PATTERNS = {
		"M.S.A.M.S",
		"S.M.A.S.M",
		"M.M.A.S.S",
		"S.S.A.M.M"
	}
}

-- Read and process file
local function readLinesFromFile(filename)
	-- open file for processing
	local file = io.open(filename, "r")
	if not file then
		error("Could not open file " .. filename)
	end

	local lines = {}
	for line in file:lines() do
		table.insert(lines, line)
	end

	file:close()
	return lines
end

-- Convert lines of characters into a 2d array
local function convertLinesToArray(lines)
	local grid = {}
	for _, line in ipairs(lines) do
		local row = {}
		for char in line:gmatch(".") do
			table.insert(row, char)
		end
		table.insert(grid, row)
	end

	return grid
end

-- Inline the grid segment
local function inlineSubGridAtIndex(grid, row, col)
    local sb = {}

    for i = row, row + Config.SUB_GRID_SIZE - 1 do
        for j = col, col + Config.SUB_GRID_SIZE - 1 do
            table.insert(sb, grid[i][j])
        end
    end

    return table.concat(sb)
end

-- Match the pattern
local function matchesAnyPattern(text)

	for _, pattern in pairs(Config.PATTERNS) do
		if string.match(text, pattern) then
			return true
		end
	end

	return false
end

-- Count the pattern matches
local function countPatternOccurrences(grid)
	local rows = #grid
	local cols = #grid[1]

	local count = 0

	for i = 1, (rows - Config.SUB_GRID_SIZE + 1) do
		for j = 1, (cols - Config.SUB_GRID_SIZE + 1) do
			local s = inlineSubGridAtIndex(grid, i, j)
			if matchesAnyPattern(s) then
				count = count + 1
			end
		end
	end

	return count
end

-- Day four, part two --
local function main(filename)
	if not filename then
		error("Usage: lua Day04.lua <path-to-input-file>")
	end

	-- read file line by line
	local lines = readLinesFromFile(filename)

	-- convert file input to 2d array
	local grid = convertLinesToArray(lines)

	-- count matches
	local count = countPatternOccurrences(grid)

	print("Count of matches: " .. count)
	
end

-- Entry point
main(arg[1])
