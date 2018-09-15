export class User {
    private id: number;
    private username: string;
	private deviceId: string;

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}
	public get Username(): string {
		return this.username;
	}
	public set Username(value: string) {
		this.username = value;
	}
	public get DeviceId(): string {
		return this.deviceId;
	}
	public set DeviceId(value: string) {
		this.deviceId = value;
	}
}