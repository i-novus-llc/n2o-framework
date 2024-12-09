import { Dispatch } from 'redux'

type CheckboxCellModel = Record<string, boolean>

export interface Props {
    visible: boolean
    disabled: boolean
    model: CheckboxCellModel
    fieldKey: string
    id: string
    callAction(model: CheckboxCellModel): void
    url: string
    target: string
    dispatch: Dispatch
    tooltipFieldId?: string
    placement?: string
}
