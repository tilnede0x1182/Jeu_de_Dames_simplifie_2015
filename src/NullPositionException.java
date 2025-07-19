class NullPositionException extends RuntimeException {
	public NullPositionException () {
		super("Aucune position possible.");
	}
}