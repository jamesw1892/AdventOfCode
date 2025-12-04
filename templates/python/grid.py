class Grid:
    """
    Store a 2D grid of characters and provide common operations on them.

    We always use y first because it is used first when indexing.
    """

    def __init__(self, filename: str):
        """
        Create the grid by reading the file with given name. Assume each line
        is a row of the grid and each character is a cell on each row.
        """
        with open(filename) as f:
            self.grid: list[list[str]] = [
                [char for char in line.strip()] for line in f.readlines()
            ]
        self.height: int = len(self.grid)
        self.width: int = len(self.grid[0])
        assert all(self.width == len(self.grid[y]) for y in range(1, self.height)), "Grid not a rectangle"

    def are_coordinates_on_grid(self, y: int, x: int) -> bool:
        """
        Return whether the given coordinates are on the grid (within bounds).
        """

        return 0 <= y < self.height and 0 <= x < self.width

    def neighbour_indices_4(self, y: int, x: int) -> list[tuple[int, int]]:
        """
        Return a list of the indices of the neighbours (orthogonally adjacent -
        the cells up, down, left, and right) of the cell with given coordinates.
        The result is a list of up to 4 elements (fewer if the given coordinates
        are on the edge of the grid) where each element is a tuple (y, x).
        """

        indices: list[tuple[int, int]] = [(y-1, x), (y, x-1), (y+1, x), (y, x+1)]
        return list(filter(lambda coords: self.are_coordinates_on_grid(coords[0], coords[1]), indices))

    def neighbour_indices_5(self, y: int, x: int) -> list[tuple[int, int]]:
        """
        Return a list of the indices of the neighbours (orthogonally adjacent -
        the cells up, down, left, and right) of the cell with given coordinates
        and the cell itself. The result is a list of up to 5 elements (fewer if
        the given coordinates are on the edge of the grid) where each element is
        a tuple (y, x).
        """

        neighbour_indices_4: list[tuple[int, int]] = self.neighbour_indices_4(y, x)
        if self.are_coordinates_on_grid(y, x):
            return neighbour_indices_4 + [(y, x)]
        return neighbour_indices_4

    def neighbour_indices_8(self, y: int, x: int):
        """
        Return a list of the indices of the neighbours (orthogonally or
        diagonally adjacent) of the cell with given coordinates. The result is
        a list of up to 8 elements (fewer if the given coordinates are on the
        edge of the grid) where each element is a tuple (y, x).
        """

        neighbour_indices_4: list[tuple[int, int]] = self.neighbour_indices_4(y, x)
        extra_indices: list[tuple[int, int]] = [(y-1, x-1), (y-1, x+1), (y+1, x-1), (y+1, x+1)]
        return neighbour_indices_4 + list(filter(lambda coords: self.are_coordinates_on_grid(coords[0], coords[1]), extra_indices))

    def neighbour_indices_9(self, y: int, x: int):
        """
        Return a list of the indices of the neighbours (orthogonally or
        diagonally adjacent) of the cell with given coordinates and the cell
        itself. The result is a list of up to 9 elements (fewer if the given
        coordinates are on the edge of the grid) where each element is a tuple
        (y, x).
        """

        neighbour_indices_8: list[tuple[int, int]] = self.neighbour_indices_8(y, x)
        if self.are_coordinates_on_grid(y, x):
            return neighbour_indices_8 + [(y, x)]
        return neighbour_indices_8

    def neighbours(self, indices_method, y: int, x: int) -> list[str]:
        """
        Given coordinates of a cell and another method to call with these
        coordinates to get all the indices of interest, turn them into the
        characters in those locations
        """

        return list(map(lambda yx: self.grid[yx[0]][yx[1]], indices_method(y, x)))

    def __getitem__(self, i):
        return self.grid.__getitem__(i)
