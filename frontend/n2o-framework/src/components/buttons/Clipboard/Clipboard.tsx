import React from 'react'
import { useDispatch } from 'react-redux'

import { FactoryStandardButton, type Props as FactoryStandardButtonProps } from '../FactoryStandardButton'
import { ICON_POSITIONS } from '../../snippets/IconContainer/IconContainer'
import { Condition } from '../../../ducks/toolbar/Toolbar'
import { ModelPrefix } from '../../../core/datasource/const'
import { useModel } from '../../core/hooks/useModel'
import { propsResolver } from '../../../core/Expression/propsResolver'
import { addAlert } from '../../../ducks/alerts/store'
import { GLOBAL_KEY, Severity } from '../../../ducks/alerts/constants'
import { id } from '../../../utils/id'

export enum Type {
    TEXT = 'text',
    HTML = 'html',
}

interface Props extends FactoryStandardButtonProps {
    src: string
    data: string
    icon: string
    iconPosition: ICON_POSITIONS
    hint: string
    hintPosition: string
    conditions: Condition
    model: ModelPrefix
    message: string
    type: Type
}

export function Clipboard(props: Props) {
    const {
        datasource,
        data,
        type,
        model: modelPrefix,
        message = 'Скопировано в буфер обмена',
        disabled = false,
    } = props

    const model = useModel(datasource, modelPrefix)
    const copied = propsResolver(data, model)

    const dispatch = useDispatch()

    const onClick = async () => {
        if (!copied) { return }

        try {
            if (type === Type.HTML) {
                // Для HTML создаем blob с соответствующим MIME-типом
                const htmlBlob = new Blob([String(copied)], { type: 'text/html' })
                const plainTextBlob = new Blob([String(copied)], { type: 'text/plain' })

                // Создаем ClipboardItem с обоими типами данных
                const clipboardItem = new ClipboardItem({ 'text/html': htmlBlob, 'text/plain': plainTextBlob })

                await navigator.clipboard.write([clipboardItem])
            } else {
                // Стандартное копирование текста
                await navigator.clipboard.writeText(String(copied))
            }

            if (message) {
                dispatch(addAlert(GLOBAL_KEY, {
                    id: id(),
                    text: message,
                    severity: Severity.SUCCESS,
                    closeButton: true,
                    timeout: 3000,
                    placement: GLOBAL_KEY,
                }))
            }
        } catch (error) {
            console.error('Ошибка копирования: ', error)
        }
    }

    return <FactoryStandardButton {...props} onClick={onClick} disabled={disabled || !copied} />
}
