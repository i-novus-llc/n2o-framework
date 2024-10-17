import { useDispatch } from 'react-redux'

import { remove } from '../../ducks/overlays/store'
import { failOperation, successOperation } from '../../ducks/api/Operation'
import { ERROR } from '../../ducks/api/utils/stopTheSequence'

interface Operation {
    id: string
    type: string
    key: string
    buttonId: string
}

export const useConfirmEffects = (id: string, operation: Operation) => {
    const dispatch = useDispatch()
    const { id: operationId, type, key, buttonId } = operation

    const onCancel = () => { dispatch(remove()) }
    const onConfirm = () => {
        onCancel()
        dispatch(successOperation(type, operationId, 'ok', { key, buttonId }))
    }
    const onDeny = () => {
        dispatch(failOperation(type, operationId, `Отмена подтверждения ${ERROR}`, { key, buttonId }))
        onCancel()
    }

    return {
        onCancel,
        onConfirm,
        onDeny,
    }
}
