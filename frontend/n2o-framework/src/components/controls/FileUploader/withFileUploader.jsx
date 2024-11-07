import React, { Component } from 'react'
import axios from 'axios'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import isArray from 'lodash/isArray'
import reduce from 'lodash/reduce'
import every from 'lodash/every'
import some from 'lodash/some'
import get from 'lodash/get'
import isFunction from 'lodash/isFunction'
import has from 'lodash/has'
import find from 'lodash/find'

import { WithPropsResolver } from '../../../core/Expression/withResolver'
import { parseExpression } from '../../../core/Expression/parse'
import { id } from '../../../utils/id'

import { deleteFile, everyIsValid, post } from './utils'

/**
 * @type {Function} ReturnedComponent
 */
const FileUploaderControl = (WrappedComponent) => {
    class ReturnedComponent extends Component {
        constructor(props) {
            super(props)

            this.state = {
                files: props.files || [],
                imgFiles: [],
                imgError: {},
            }

            this.requests = {}

            this.handleDrop = this.handleDrop.bind(this)
            this.onDropRejected = this.onDropRejected.bind(this)
            this.handleImagesDrop = this.handleImagesDrop.bind(this)
            this.handleRemove = this.handleRemove.bind(this)
            this.handleChange = this.handleChange.bind(this)
            this.startUpload = this.startUpload.bind(this)
            this.onStartUpload = this.onStartUpload.bind(this)
            this.getUploaderProps = this.getUploaderProps.bind(this)
            this.resolveUrl = this.resolveUrl.bind(this)
            this.onDragEnter = this.onDragEnter.bind(this)
            this.onDragLeave = this.onDragLeave.bind(this)
            this.onError = this.onError.bind(this)
            this.clearState = this.clearState.bind(this)
        }

        componentDidMount() {
            const { mapper, value, model, fieldKey } = this.props
            const { files } = this.state

            this.setState({
                files: mapper
                    ? mapper(value)
                    : this.mapFiles(!isEmpty(value) ? value : files),
            })

            if (isEmpty(value) && model) {
                const files = model[fieldKey] || []

                this.setState({
                    files: Array.isArray(files)
                        ? this.mapFiles(files)
                        : this.mapFiles([files]),
                })
            }
        }

        componentDidUpdate(prevProps) {
            const { value, files, mapper } = this.props
            const { files: stateFiles } = this.state

            if (!isEqual(prevProps.value, value)) {
                if (!value) {
                    this.setState({ files: [] })
                }

                const newFiles = mapper
                    ? mapper(value || [])
                    : this.mapFiles(value || [])

                const hasUpdate = !every(newFiles, file => some(stateFiles, file))

                if (hasUpdate) {
                    this.setState({ files: newFiles })
                }
            } else if (!isEqual(prevProps.files, files)) {
                this.setState({
                    files: mapper ? mapper(files || []) : this.mapFiles(files || []),
                })
            }
        }

        clearState() {
            this.setState({ files: [] })
        }

        mapFiles(files) {
            if (!files) {
                return
            }

            const currentFiles = isArray(files) ? files : [files]

            // eslint-disable-next-line consistent-return
            return currentFiles.map(file => this.fileAdapter(file))
        }

        fileAdapter(file) {
            const {
                valueFieldId,
                labelFieldId,
                statusFieldId,
                sizeFieldId,
                responseFieldId,
                urlFieldId,
            } = this.props

            return {
                id: file[valueFieldId],
                name: file[labelFieldId] || file.fileName,
                status: file[statusFieldId],
                size: file[sizeFieldId],
                response: file[responseFieldId],
                link: file[urlFieldId],
            }
        }

        /**
         * Получение Url из expression
         * @returns {*}
         */
        resolveUrl(url) {
            if (!parseExpression(url)) {
                return url
            }

            const { propsResolver } = this.props

            // TODO разобраться что-за контекст такой тут ожидается
            const { _reduxForm } = this.context
            const { resolveModel } = _reduxForm

            return propsResolver(url, resolveModel)
        }

        /**
         * Return props
         */
        getUploaderProps() {
            const { files, imgFiles, uploading, uploaderClass, imgError } = this.state
            const { multi } = this.props

            return {
                ...this.props,
                files,
                imgFiles,
                uploading,
                requests: this.requests,
                multiple: multi,
                uploaderClass,
                onFocus: () => {},
                onBlur: () => {},
                onDrop: this.handleDrop,
                onDropRejected: this.onDropRejected,
                onImagesDrop: this.handleImagesDrop,
                onDragLeave: this.onDragLeave,
                onDragEnter: this.onDragEnter,
                onRemove: this.handleRemove,
                onStartUpload: this.onStartUpload,
                imgError,
            }
        }

        handleDrop(data) {
            const { onChange, autoUpload, onBlur, multi } = this.props
            const { files: stateFiles } = this.state
            const preparedFiles = data.map((file) => {
                file.id = id()
                file.percentage = 0

                return file
            })
            const files = multi ? [...stateFiles, ...preparedFiles] : preparedFiles

            this.setState(
                {
                    files,
                    uploaderClass: null,
                },
                () => {
                    if (autoUpload) {
                        this.startUpload(data)
                    } else {
                        onChange(data)
                        onBlur(data)
                    }
                },
            )
        }

        /**
         * @param {Array<File>} files
         */
        onDropRejected(files) {
            const { accept } = this.props
            const { files: stateFiles } = this.state
            const errorText = `Ошибка формата файла. Ожидаемый тип: ${accept}`

            this.setState({
                files: [...stateFiles, ...files.map((file) => {
                    file.error = errorText
                    file.id = id()
                    file.percentage = 0

                    return file
                })],
            })
        }

        /**
         * Загрузка изображений в state
         * @param files
         */
        handleImagesDrop(files) {
            const { accept, t } = this.props
            let errorText = `${t('imageUploadAvailableImageTypes')} JPG/PNG/SVG`

            if (accept) {
                errorText = `${t('imageUploadAvailableImageTypes')} ${accept}`
            }

            if (everyIsValid(files) && files.length !== 0) {
                this.setState({
                    imgError: {},
                })

                const { imgFiles } = this.state

                this.setState({
                    imgFiles: imgFiles.concat(files),
                })

                this.handleDrop(files)
            } else {
                this.setState({
                    imgError: {
                        message: errorText,
                    },
                })
            }
        }

        /**
         * Удаление из стейта
         * @param index
         * @param id
         */
        handleRemove(index, id) {
            const {
                value = [],
                multi,
                valueFieldId,
                onChange,
                onBlur,
                deleteUrl,
                onDelete,
                deleteRequest,
            } = this.props

            const { files, imgFiles } = this.state

            this.setState({
                imgError: {},
            })

            const fileToBeDeleted = find(files, ({ id: idFromState }) => idFromState === id)
            const isUploading = !has(fileToBeDeleted, 'response') && !has(fileToBeDeleted, 'error')

            const fileDeletionExecutor = () => {
                if (isUploading) {
                    fileToBeDeleted.cancelSource.cancel()
                }

                if (!deleteUrl) {
                    return
                }

                if (isFunction(deleteRequest)) {
                    deleteRequest(id)

                    return
                }

                deleteFile(this.resolveUrl(deleteUrl), id)
                onDelete(index, id)
            }

            fileDeletionExecutor()

            const newFiles = files.slice()
            const newImgFiles = imgFiles.slice()

            newFiles.splice(index, 1)
            newImgFiles.splice(index, 1)
            this.setState({
                files: [...newFiles],
                imgFiles: [...newImgFiles],
            })

            if (value) {
                const newValue = multi
                    ? value.filter(f => f[valueFieldId] !== id)
                    : null

                onChange(newValue)
                onBlur(newValue)
            }
        }

        /**
         * Изменение компонента
         */
        handleChange(newFile) {
            const { value, multi, onChange } = this.props

            onChange(multi ? [...(value || []), newFile] : newFile)
        }

        /**
         * Start upload files
         * @param files
         */
        startUpload(files) {
            const {
                labelFieldId,
                sizeFieldId,
                requestParam,
                uploadUrl,
                onStart,
                uploadRequest,
            } = this.props

            const { files: preparedFilesFromState } = this.state
            const url = this.resolveUrl(uploadUrl)

            this.setState({
                uploading: reduce(
                    preparedFilesFromState,
                    (acc, file) => {
                        const { id, error } = file

                        if (!has(file, 'status')) {
                            acc = { ...acc, [id]: !error }
                        }

                        return acc
                    },
                    {},
                ),
            })

            files.forEach((file) => {
                if (file.error) {
                    // Не загружаем файлы, которые не прошли префильтры

                    return
                }
                if (!this.requests[file.id]) {
                    const onProgress = this.onProgress.bind(this, file.id)
                    const onUpload = this.onUpload.bind(this, file.id)
                    const onError = this.onError.bind(this, file.id)

                    file.cancelSource = axios.CancelToken.source()

                    if (labelFieldId !== 'name') {
                        file[labelFieldId] = file.name
                    }
                    if (sizeFieldId !== 'size') {
                        file[sizeFieldId] = file.size
                    }

                    const formData = new FormData()

                    formData.append(requestParam, file)
                    onStart(file)

                    if (isFunction(uploadRequest)) {
                        uploadRequest(formData, onProgress, onUpload, onError)
                    } else {
                        this.requests[file.id] = undefined

                        post(
                            url,
                            formData,
                            onProgress,
                            onUpload,
                            onError,
                            file.cancelSource,
                        )
                    }
                }
            })
        }

        /**
         * Change upload progress
         * @param id
         * @param event
         */
        onProgress(id, event) {
            if (event.lengthComputable) {
                this.onLoading(event.loaded / event.total, id)
            }
        }

        /**
         * Loading event
         * @param percentage
         * @param id
         */
        onLoading(percentage, id) {
            const { files } = this.state

            this.setState({
                files: [
                    ...files.map((file) => {
                        if (file.id === id) {
                            file.percentage = percentage
                        }

                        return file
                    }),
                ],
            })
        }

        /**
         * Call upload function
         */
        onStartUpload() {
            const { files } = this.state

            this.startUpload(files)
        }

        /**
         * Upload event
         * @param id
         * @param response
         */
        onUpload(id, response) {
            const { onSuccess } = this.props

            if (response.status < 200 || response.status >= 300) {
                this.onError(id, response.statusText, response.status)
            } else {
                const file = response.data
                const { files, uploading } = this.state

                this.setState({
                    files: [
                        ...files.map((item) => {
                            if (item.id === id) {
                                return {
                                    ...this.fileAdapter(file),
                                    loading: false,
                                }
                            }

                            return item
                        }),
                    ],
                    uploading: {
                        ...uploading,
                        [id]: false,
                    },
                })
                this.requests[id] = undefined
                onSuccess(response)
                this.handleChange(file)
            }
        }

        onError(id, error) {
            const { responseFieldId, onError } = this.props

            const { uploading, files } = this.state

            if (uploading) {
                uploading[id] = false
            }
            this.setState({
                uploading,
                // eslint-disable-next-line array-callback-return
                ...files.map((file) => {
                    if (file.id === id) {
                        let formattedError

                        if (onError) {
                            formattedError = onError(error)
                        } else {
                            formattedError = (
                                get(error, `response.data[${responseFieldId}]`, null) ||
                                error.message ||
                                error.status
                            )
                        }

                        file.error = formattedError
                    }
                }),
            })
        }

        onDragEnter() {
            this.setState({
                uploaderClass: 'n2o-file-uploader-event-drag-enter',
            })
        }

        onDragLeave() {
            this.setState({
                uploaderClass: null,
            })
        }

        render() {
            return <WrappedComponent {...this.getUploaderProps()} />
        }
    }

    ReturnedComponent.contextTypes = {
        _reduxForm: PropTypes.string,
    }

    ReturnedComponent.defaultProps = {
        t: () => {},
        requestParam: 'file',
        visible: true,
        icon: 'fa fa-upload',
        statusBarColor: 'success',
        multi: true,
        disabled: false,
        autoUpload: true,
        showSize: true,
        value: [],
        sizeFieldId: 'size',
        onChange: () => {},
        onBlur: () => {},
        onStart: () => {},
        onSuccess: () => {},
        onDelete: () => {},
    }

    return WithPropsResolver(ReturnedComponent)
}

export default FileUploaderControl
