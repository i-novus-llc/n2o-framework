import React, { ReactNode } from 'react'
import flowRight from 'lodash/flowRight'
import { withTranslation } from 'react-i18next'

import withFileUploader from '../FileUploader/withFileUploader'
import { mapToNumeric } from '../../../tools/helpers'

import { ImageUpload, type Props as ImageUploadProps } from './ImageUpload'
import { createCustomSizes, getSize, getCurrentLabel, defaultDropZone, type ImgError } from './utils'
import { type File } from './ImageUploaderItem'
import { Shape } from './ImageUploaderItem'

interface Props extends ImageUploadProps {
    icon: string
    label: string
    children: ReactNode
    showTooltip: boolean
    imgError: ImgError
    width: string
    height: string
    iconSize: string
    unit: string
    canDelete: boolean
    shape: Shape
    lightbox: boolean
    accept: string
    files: File[]
}

function ImageUploader({
    icon,
    label,
    children,
    showTooltip = true,
    imgError,
    width: propsWidth,
    height: propsHeight,
    iconSize: propsIconSize,
    unit,
    canDelete = true,
    shape,
    lightbox = false,
    accept = 'image/*',
    ...rest
}: Props) {
    const { width, height, iconSize } = mapToNumeric(
        {
            width: propsWidth,
            height: propsHeight,
            iconSize: propsIconSize,
        },
    )

    const currentLabel = getCurrentLabel(imgError, label) as string
    const size = createCustomSizes(width, height, iconSize, unit)
    const component = children || defaultDropZone(icon, currentLabel, size)

    return (
        <ImageUpload
            componentClass="n2o-drop-zone"
            customUploaderSize={getSize(size, 'uploader')}
            showTooltip={showTooltip}
            imgError={imgError}
            canDelete={canDelete}
            shape={shape}
            lightbox={lightbox}
            accept={accept}
            {...rest}
        >
            { component }
        </ImageUpload>
    )
}

export default flowRight(
    withTranslation(),
    withFileUploader,
)(ImageUploader)
