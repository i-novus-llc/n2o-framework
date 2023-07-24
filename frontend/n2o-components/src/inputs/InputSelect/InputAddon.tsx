import React from 'react'

import { TOption } from './types'
/**
 * InputAddon - дополнение с иконкой/картинкой для инпута {@Link InputSelectGroup}
 * @reactProps {object} item - выбранный элемент
 * @reactProps {string} iconFieldId - поле иконки
 * @reactProps {string} imageFieldId - поле картинки
 * @return {null}
 */

type Props = {
    iconFieldId: string,
    imageFieldId: string,
    item: TOption | null,
    setRef?(): void,
}

export function InputAddon({ item, iconFieldId, imageFieldId, setRef }: Props) {
    if (!item) { return null }

    return item[iconFieldId as keyof TOption] || item[imageFieldId as keyof TOption] ? (
        <span className="selected-item selected-item--single" ref={setRef}>
            {iconFieldId && item[iconFieldId as keyof TOption] && <i className={item[iconFieldId as keyof TOption]} />}
            {/* eslint-disable-next-line jsx-a11y/alt-text */}
            {imageFieldId && item[imageFieldId as keyof TOption] && <img src={item[imageFieldId as keyof TOption]} />}
        </span>
    ) : null
}
