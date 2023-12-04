import { useDispatch } from 'react-redux'

import { remove } from '../../ducks/overlays/store'
import { failOperation, successOperation } from '../../ducks/api/Operation'

export const useConfirmEffects = (id: string, operation: { id: string, type: string }) => {
    const dispatch = useDispatch()
    const { id: operationId, type } = operation

    const onCancel = () => {
        dispatch(remove(id))
    }
    const onConfirm = () => {
        dispatch(successOperation(type, operationId))
        onCancel()
    }
    const onDeny = () => {
        dispatch(failOperation(type, operationId, null))
        onCancel()
    }

    return {
        onCancel,
        onConfirm,
        onDeny,
    }
}
