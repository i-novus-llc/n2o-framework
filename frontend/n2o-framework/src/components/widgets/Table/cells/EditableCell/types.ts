import { Model } from '../../../../../ducks/models/selectors'

import { ControlProps } from './Control'

export interface EditableCellProps {
    visible: boolean
    control: ControlProps['control'] & { id: string }
    editable: boolean
    disabled: boolean
    format: string
    fieldKey: string
    model: Model
    callAction(model: Model): void
}
