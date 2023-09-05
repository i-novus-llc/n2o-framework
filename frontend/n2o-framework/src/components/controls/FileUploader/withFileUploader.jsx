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

import evalExpression, { parseExpression } from '../../../utils/evalExpression'
import { id } from '../../../utils/id'

import { deleteFile, everyIsValid, post } from './utils'

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
            if (!files) { return }
            let currentFiles = []

            if (!isArray(files)) {
                currentFiles = [files]
            } else {
                currentFiles = files
            }

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
            const expression = parseExpression(url)

            if (!expression) {
                return url
            }
            const { _reduxForm } = this.context
            const { resolveModel } = _reduxForm

            return evalExpression(expression, resolveModel)
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

        /**
         * Загрузка файлов в state
         * @param files
         */

        handleDrop(files) {
            const { onChange, autoUpload, onBlur } = this.props
            const { files: stateFiles } = this.state

            this.setState(
                {
                    files: [
                        ...stateFiles,
                        ...files.map((file) => {
                            file.id = id()
                            file.percentage = 0

                            return file
                        }),
                    ],
                    uploaderClass: null,
                },
                () => {
                    if (autoUpload) {
                        this.startUpload(files)
                    } else {
                        onChange(files)
                        onBlur(files)
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
                    preparedFilesFromState, (acc, file) => {
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
                        this.requests[file.id] = post(
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
                        let formattedError = null

                        if (onError) {
                            formattedError = onError(error)
                        } else {
                            formattedError =
                get(error, `response.data[${responseFieldId}]`, null) ||
                error.message ||
                error.status
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

    ReturnedComponent.propTypes = {
        /**
         * Ключ ID из даныых
         */
        valueFieldId: PropTypes.string,
        /**
         * Ключ label из данных
         */
        labelFieldId: PropTypes.string,
        /**
         * Ключ status из данных
         */
        statusFieldId: PropTypes.string,
        /**
         * Ключ size из данных
         */
        sizeFieldId: PropTypes.string,
        /**
         * Ключ response из даннах
         */
        responseFieldId: PropTypes.string,
        /**
         * Ключ url из данных
         */
        urlFieldId: PropTypes.string,
        /**
         * Url для загрузки файла
         */
        uploadUrl: PropTypes.string,
        /**
         * Url для удаления файла
         */
        deleteUrl: PropTypes.string,
        /**
         * Флаг мульти выбора файлов
         */
        multi: PropTypes.bool,
        /**
         * Массив разрешенных расширеней файлов
         */
        accept: PropTypes.arrayOf(PropTypes.string),
        /**
         * Массив файлов
         */
        files: PropTypes.arrayOf(PropTypes.object),
        /**
         * Значение
         */
        value: PropTypes.arrayOf(PropTypes.object),
        /**
         * Флаг автоматической загрузки файлов после выбора
         */
        autoUpload: PropTypes.bool,
        /**
         * Максимальный размер файла
         */
        maxSize: PropTypes.number,
        /**
         * Минимальный размер файла
         */
        minSize: PropTypes.number,
        /**
         * Label контрола
         */
        label: PropTypes.string,
        /**
         * Название отправлякмого параметра
         */
        requestParam: PropTypes.string,
        /**
         * Флаг видимости
         */
        visible: PropTypes.bool,
        /**
         * Флаг активности
         */
        disabled: PropTypes.bool,
        /**
         * Иконка рядом с label
         */
        icon: PropTypes.string,
        /**
         * Цвет статус бара
         */
        statusBarColor: PropTypes.string,
        /**
         * Объект стилей кнопки 'Сохранить'
         */
        saveBtnStyle: PropTypes.object,
        /**
         * Флаг показа размера файла
         */
        showSize: PropTypes.bool,
        /**
         * Callback на изменение
         */
        onChange: PropTypes.func,
        /**
         * Класс контрола
         */
        className: PropTypes.string,
        /**
         * Mapper значения
         */
        mapper: PropTypes.func,
        children: PropTypes.oneOfType([PropTypes.func, PropTypes.node]),
        /**
         * Callback на старт загрузки файла
         */
        onStart: PropTypes.func,
        /**
         * Callback на успешное завершение загрузки файла
         */
        onSuccess: PropTypes.func,
        /**
         * Callback на ошибку при загрузке
         */
        onError: PropTypes.func,
        /**
         * Callback на удаление файла
         */
        onDelete: PropTypes.func,
        /**
         * Кастомный запрос отправки файла
         */
        uploadRequest: PropTypes.func,
        /**
         * Кастомный запрос удаления файла
         */
        deleteRequest: PropTypes.func,
        onBlur: PropTypes.func,
        model: PropTypes.object,
        fieldKey: PropTypes.string,
        t: PropTypes.func,
    }

    return ReturnedComponent
}

export default FileUploaderControl
