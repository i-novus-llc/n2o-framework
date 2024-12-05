export interface Props {
    visible: boolean
    max: number
    half: boolean
    showTooltip: boolean
    fieldKey: string
    id: string
    readonly: boolean
    model: Record<string, number>
    callAction(model: Record<string, unknown>): void
}
