import React, { CSSProperties } from 'react'
import map from 'lodash/map'

import { ImageUploaderItem, type File } from './ImageUploaderItem'

export interface Props {
    files: File[]
    onRemove(index: number, id: string): void
    uploading: Record<string, boolean>
    showTooltip?: boolean
    canDelete?: boolean
    statusBarColor?: string
    autoUpload?: boolean
    showSize?: boolean
    showName?: boolean
    lightBox?: boolean
    disabled?: boolean
    listType?: 'card' | 'image'
    customUploaderSize?: CSSProperties
    shape?: 'circle' | 'square'

}

export const ImageUploaderList = ({
    files,
    onRemove,
    uploading,
    showTooltip,
    canDelete,
    ...rest
}: Props) => {
    return (
        <>
            {map(files, (file, index) => (
                <ImageUploaderItem
                    file={file}
                    key={file.id}
                    onRemove={onRemove}
                    index={index}
                    loading={uploading?.[file.id]}
                    showTooltip={showTooltip}
                    canDelete={canDelete}
                    {...rest}
                />
            ))}
        </>
    )
}

export default ImageUploaderList
