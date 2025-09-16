import React, { useState, CSSProperties } from 'react'
import classNames from 'classnames'
import { Modal, Tooltip } from 'reactstrap'
import { Spinner, SpinnerType } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import { convertSize } from '../FileUploader/utils'

export interface File extends Blob {
    id: string
    url: string
    error?: string
    link: string
    name: string
    response?: string
    percentage?: number
}

export type Shape = 'circle' | 'square'

export interface Props {
    file: File
    onRemove(index: number, id: string): void
    showSize?: boolean
    showName?: boolean
    index: number
    loading?: boolean
    lightBox?: boolean
    listType?: 'card' | 'image'
    customUploaderSize?: CSSProperties
    showTooltip?: boolean
    canDelete?: boolean
    shape?: Shape
}

const getImageSrc = (file: File) => {
    if (file.error !== undefined) {
        return ''
    }
    if (file.link !== undefined) {
        return file.link
    }

    return URL.createObjectURL(file)
}

export const ImageUploaderItem = ({
    file,
    onRemove,
    showSize,
    showName,
    index,
    loading,
    lightBox,
    listType,
    customUploaderSize,
    showTooltip,
    canDelete,
    shape,
}: Props) => {
    const [tooltipOpen, setTooltipOpen] = useState(false)
    const [modalOpen, setModalOpen] = useState(false)

    const openModal = () => setModalOpen(true)
    const closeModal = () => setModalOpen(false)

    const toggleTooltip = () => setTooltipOpen(!tooltipOpen)

    const imgSrc = getImageSrc(file)

    const modifyStyle = (size?: CSSProperties) => (listType === 'card' && shape !== 'circle' ? {
        ...size,
        maxWidth: undefined,
        width: undefined,
    } : size)

    return (
        <div className="n2o-image-uploader-files-item" style={modifyStyle(customUploaderSize)}>
            <span
                className={classNames('n2o-file-uploader-files-item-info', { 'with-info': listType === 'card' && (showSize || showName) })}
            >
                <a
                    title={file.name}
                    target="_blank"
                    id={`tooltip-${file.id}`}
                    className={classNames('n2o-image-uploader-link', {
                        'n2o-image-uploader-item-error': file.error,
                        'single-img': listType === 'image',
                        'n2o-image-uploader-link--shape-circle': shape === 'circle',
                    })}
                    style={modifyStyle(customUploaderSize)}
                >
                    <div className={classNames('n2o-image-uploader__watch', {
                        'single-img': listType === 'image',
                        'n2o-image-uploader__watch--shape-circle': shape === 'circle',
                    })}
                    >
                        <div className="n2o-image-uploader__watch--icons-container">
                            {lightBox && file.error === undefined && (
                                <span>
                                    <i onClick={openModal} className="n2o-image-uploader__watch--eye fa fa-eye" />
                                </span>
                            )}
                            {canDelete && (
                                <span>
                                    <i
                                        onClick={() => onRemove(index, file.id)}
                                        className="n2o-image-uploader__watch--trash fa fa-trash"
                                    />
                                </span>
                            )}
                        </div>
                    </div>
                    <img
                        className={classNames('n2o-image-uploader--img', {
                            'n2o-image-uploader--img--shape-circle': shape === 'circle',
                        })}
                        src={imgSrc}
                        alt={file.name}
                        style={{ ...customUploaderSize, height: undefined }}
                    />
                </a>
                {showTooltip && (file.error || file.response) && (
                    <Tooltip isOpen={tooltipOpen} target={`tooltip-${file.id}`} toggle={toggleTooltip}>
                        {file.response || file.error}
                    </Tooltip>
                )}
                <div className="n2o-image-uploader-img-info">
                    {listType === 'card' && showName && (
                        <span className="n2o-image-uploader-img-info__file-name">{file.name}</span>
                    )}
                    <span className="n2o-image-uploader-img-info__file-size">
                        {listType === 'card' && showSize && <span>{convertSize(file.size)}</span>}
                        {loading && <Spinner loading={loading} className="ml-2" type={SpinnerType.inline} size="sm" />}
                    </span>
                </div>
            </span>
            <Modal
                isOpen={modalOpen}
                backdrop
                centered
                toggle={() => setModalOpen(false)}
                className="n2o-image-uploader__modal"
            >
                <div className="n2o-image-uploader__modal--body">
                    <i onClick={closeModal} className="n2o-image-uploader__modal--icon-close fa fa-times" />
                    <img className="n2o-image-uploader__modal--image" src={imgSrc} alt="modal" />
                </div>
            </Modal>
        </div>
    )
}

export default ImageUploaderItem
