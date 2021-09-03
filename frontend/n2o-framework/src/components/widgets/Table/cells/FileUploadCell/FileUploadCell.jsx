import React from 'react'
import PropTypes from 'prop-types'
import { compose } from 'recompose'
import { withTranslation } from 'react-i18next'
import { Button } from 'reactstrap'
import classNames from 'classnames'

import FileUploader from '../../../../controls/FileUploader/FileUploader'
import withFileUploader from '../../../../controls/FileUploader/withFileUploader'

function FileUploadCellComponent(props) {
    const { multi, files, t, showSize, label, uploadIcon, deleteIcon, className } = props

    const isButtonVisible = !!(multi || files.length === 0)

    return (
        <div className={classNames('file-upload-cell-wrapper', { showSize }, className)}>
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
        </div>

    )
}

FileUploadCellComponent.propTypes = {
    uploadUrl: PropTypes.string,
    deleteUrl: PropTypes.string,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    messageFieldId: PropTypes.string,
    urlFieldId: PropTypes.string,
    requestParam: PropTypes.string,
    className: PropTypes.string,
    showSize: PropTypes.bool,
    multi: PropTypes.bool,
    ajax: PropTypes.bool,
    accept: PropTypes.string,
    label: PropTypes.string,
    uploadIcon: PropTypes.string,
    deleteIcon: PropTypes.string,
    files: PropTypes.array,
    t: PropTypes.func,
}

const FileUploadCell = compose(
    withFileUploader,
    withTranslation(),
)(FileUploadCellComponent)

export { FileUploadCell }
