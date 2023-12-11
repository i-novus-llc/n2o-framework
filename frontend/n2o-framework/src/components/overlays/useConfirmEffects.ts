import { useDispatch } from 'react-redux'

import { remove } from '../../ducks/overlays/store'
import { failOperation, successOperation } from '../../ducks/api/Operation'

interface Operation {
    id: string
    type: string
    key: string
    buttonId: string
}

export const useConfirmEffects = (id: string, operation: Operation) => {
    const dispatch = useDispatch()
    const { id: operationId, type, key, buttonId } = operation

    const onCancel = () => { dispatch(remove(id)) }
    const onConfirm = () => {
        dispatch(successOperation(type, operationId, 'ok', { key, buttonId }))
        onCancel()
    }
    const onDeny = () => {
        dispatch(failOperation(type, operationId, null, { key, buttonId }))
        onCancel()
    }

    return {
        onCancel,
        onConfirm,
        onDeny,
    }
}
