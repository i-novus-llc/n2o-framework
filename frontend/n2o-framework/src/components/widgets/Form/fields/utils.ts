import { IValidationResult } from '../../../../core/validation/IValidation'

export function getValidationClass(message: IValidationResult): string | false {
    if (!message) {
        return false
    }

    if (message.severity === 'success') {
        return 'is-valid'
    }

    if (message.severity === 'warning') {
        return 'has-warning'
    }

    return 'is-invalid'
}

const INDEX_PLACEHOLDER = 'index'

export const modifyOn = (
    on: string[],
    parentIndex: string,
) => on.map(key => key.replace(INDEX_PLACEHOLDER, parentIndex))

export const replaceIndex = (obj: object, index: string) => JSON.parse(
    JSON.stringify(obj).replaceAll(INDEX_PLACEHOLDER, index),
)

export const resolveControlIndexes = (control: { dataProvider?: object }, parentIndex: string) => {
    if (control.dataProvider) {
        const dataProvider = replaceIndex(control.dataProvider, parentIndex)

        return {
            ...control,
            dataProvider,
        }
    }

    return control
}
