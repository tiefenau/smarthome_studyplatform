export class Option {

	private id: number;
	private value: string;

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}
	public get Value(): string {
		return this.value;
	}
	public set Value(value: string) {
		this.value = value;
	}
}