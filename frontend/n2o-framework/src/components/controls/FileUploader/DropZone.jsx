import React from 'react'
import PropTypes from 'prop-types'

import withFileUploader from './withFileUploader'
import FileUploader from './FileUploader'

function defaultDropZone(icon, label) {
    return (
        <>
            <div className={icon} />
            {label}
        </>
    )
}

function DropZone(props) {
    const { icon, label, children } = props

    return (
        <FileUploader
            {...props}
            componentClass="n2o-drop-zone"
        >
            {children || defaultDropZone(icon, label)}
        </FileUploader>
    )
}

DropZone.defaultProps = {
    saveBtnStyle: {
        marginTop: '10px',
    },
}

DropZone.propTypes = {
    label: PropTypes.string,
    uploading: PropTypes.object,
    icon: PropTypes.string,
    files: PropTypes.arrayOf(PropTypes.object),
    className: PropTypes.string,
    onDrop: PropTypes.func,
    autoUpload: PropTypes.bool,
    onRemove: PropTypes.func,
    onStartUpload: PropTypes.func,
    saveBtnStyle: PropTypes.object,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    requestParam: PropTypes.string,
    maxSize: PropTypes.number,
    minSize: PropTypes.number,
    multiple: PropTypes.bool,
    onChange: PropTypes.func,
    children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
}

export default withFileUploader(DropZone)
