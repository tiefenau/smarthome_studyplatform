export class PfivaConfigData {
    private id: number;
    private key: string;
    private value: string;
    
    public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
    }
    public get Key(): string {
		return this.key;
	}
	public set Key(value: string) {
		this.key = value;
    }
    public get Value(): string {
		return this.value;
	}
	public set Value(value: string) {
		this.value = value;
	}
}