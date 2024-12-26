import React from 'react'
import flowRight from 'lodash/flowRight'
import { withTranslation } from 'react-i18next'
import { Button } from 'reactstrap'
import classNames from 'classnames'

import FileUploader from '../../../../controls/FileUploader/FileUploader'
import withFileUploader from '../../../../controls/FileUploader/withFileUploader'
import { DefaultCell } from '../DefaultCell'

import { type Props } from './types'

function FileUploadCellComponent(props: Props) {
    const { multi, files, t, showSize, label, uploadIcon, deleteIcon, className, disabled } = props

    const isButtonVisible = multi || files.length === 0

    return (
        <DefaultCell disabled={disabled} className={classNames('file-upload-cell-wrapper', { showSize }, className)}>
            <FileUploader
                componentClass="file-upload-cell"
                {...props}
                deleteIcon={deleteIcon || 'fa fa-trash'}
            >
                {isButtonVisible && (
                    <Button className="file-upload-cell__button">
                        {t(label || 'uploadFile')}
                        <i className={classNames(uploadIcon || 'fa fa-upload', 'ml-2')} />
                    </Button>
                )
                }
            </FileUploader>
        </DefaultCell>

    )
}

const FileUploadCell = flowRight(
    withFileUploader,
    withTranslation(),
)(FileUploadCellComponent)

export { FileUploadCell }
