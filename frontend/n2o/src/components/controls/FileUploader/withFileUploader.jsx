import React from 'react';
import PropTypes from 'prop-types';
import {
  isEmpty,
  isEqual,
  isArray,
  isString,
  reduce,
  every,
  some,
  get,
  isFunction,
} from 'lodash';
import { post, deleteFile } from './utils';
import { id } from '../../../utils/id';
import evalExpression, { parseExpression } from '../../../utils/evalExpression';

const FileUploaderControl = WrappedComponent => {
  class ReturnedComponent extends React.Component {
    constructor(props) {
      super(props);

      this.state = {
        files: props.files || [],
      };
      this.requests = {};

      this.handleDrop = this.handleDrop.bind(this);
      this.handleRemove = this.handleRemove.bind(this);
      this.handleChange = this.handleChange.bind(this);
      this.startUpload = this.startUpload.bind(this);
      this.onStartUpload = this.onStartUpload.bind(this);
      this.getUploaderProps = this.getUploaderProps.bind(this);
      this.resolveUrl = this.resolveUrl.bind(this);
      this.onDragEnter = this.onDragEnter.bind(this);
      this.onDragLeave = this.onDragLeave.bind(this);
      this.onError = this.onError.bind(this);
      this.clearState = this.clearState.bind(this);
    }

    componentDidMount() {
      const { mapper, value } = this.props;
      this.setState({
        files: mapper
          ? mapper(value)
          : this.mapFiles(!isEmpty(value) ? value : this.state.files),
      });
    }

    componentDidUpdate(prevProps) {
      const { value, files, mapper } = this.props;
      if (!isEqual(prevProps.value, value)) {
        const newFiles = mapper
          ? mapper(value || [])
          : this.mapFiles(value || []);

        const hasUpdate = !every(newFiles, file =>
          some(this.state.files, file)
        );

        hasUpdate && this.setState({ files: newFiles });
      } else if (!isEqual(prevProps.files, files)) {
        this.setState({
          files: mapper ? mapper(files || []) : this.mapFiles(files || []),
        });
      }
    }

    clearState() {
      this.setState({ files: [] });
    }

    mapFiles(files) {
      if (!files) return;
      let currentFiles = [];
      if (!isArray(files)) {
        currentFiles = [files];
      } else {
        currentFiles = files;
      }
      return currentFiles.map(file => {
        return this.fileAdapter(file);
      });
    }

    fileAdapter(file) {
      const {
        valueFieldId,
        labelFieldId,
        statusFieldId,
        sizeFieldId,
        responseFieldId,
        urlFieldId,
      } = this.props;
      return {
        id: file[valueFieldId],
        name: file[labelFieldId],
        status: file[statusFieldId],
        size: file[sizeFieldId],
        response: file[responseFieldId],
        link: file[urlFieldId],
      };
    }

    /**
     * Получение Url из expression
     * @returns {*}
     */
    resolveUrl(url) {
      const expression = parseExpression(url);
      if (!expression) {
        return url;
      }
      const resolveModel = this.context._reduxForm.resolveModel;
      return evalExpression(expression, resolveModel);
    }

    /**
     * Return props
     */
    getUploaderProps() {
      return {
        ...this.props,
        files: this.state.files,
        uploading: this.state.uploading,
        requests: this.requests,
        multiple: this.props.multi,
        uploaderClass: this.state.uploaderClass,
        onFocus: () => {},
        onBlur: () => {},
        onDrop: this.handleDrop,
        onDragLeave: this.onDragLeave,
        onDragEnter: this.onDragEnter,
        onRemove: this.handleRemove,
        onStartUpload: this.onStartUpload,
      };
    }

    /**
     * Загрузка файлов в state
     * @param files
     */
    handleDrop(files) {
      const { onChange, autoUpload } = this.props;

      this.setState(
        {
          files: [
            ...this.state.files,
            ...files.map(file => {
              file.id = id();
              file.percentage = 0;
              return file;
            }),
          ],
          uploaderClass: null,
        },
        () => {
          if (autoUpload) {
            this.startUpload(files);
          } else {
            onChange(files);
          }
        }
      );
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
        deleteUrl,
        onDelete,
        deleteRequest,
      } = this.props;

      if (deleteUrl) {
        if (isFunction(deleteRequest)) {
          deleteRequest(id);
        } else {
          deleteFile(this.resolveUrl(deleteUrl), id);
          onDelete(index, id);
        }
      }

      const newFiles = this.state.files.slice();
      newFiles.splice(index, 1);
      this.setState({
        files: [...newFiles],
      });

      if (value) {
        onChange(multi ? value.filter(f => f[valueFieldId] !== id) : null);
      }
    }

    /**
     * Изменение компонента
     */
    handleChange(newFile) {
      const { value, multi, onChange } = this.props;
      onChange(multi ? [...(value ? value : []), newFile] : newFile);
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
      } = this.props;

      const url = this.resolveUrl(uploadUrl);

      this.setState({
        uploading: reduce(files, (acc, { id }) => ({ ...acc, [id]: true }), {}),
      });

      files.map(file => {
        if (!this.requests[file.id]) {
          const onProgress = this.onProgress.bind(this, file.id);
          const onUpload = this.onUpload.bind(this, file.id);
          const onError = this.onError.bind(this, file.id);
          if (labelFieldId !== 'name') {
            file[labelFieldId] = file.name;
          }
          if (sizeFieldId !== 'size') {
            file[sizeFieldId] = file.size;
          }

          const formData = new FormData();
          formData.append(requestParam, file);
          onStart(file);

          if (isFunction(uploadRequest)) {
            uploadRequest(formData, onProgress, onUpload, onError);
          } else {
            this.requests[file.id] = post(
              url,
              formData,
              onProgress,
              onUpload,
              onError
            );
          }
        }
      });
    }

    /**
     * Change upload progress
     * @param id
     * @param event
     */
    onProgress(id, event) {
      if (event.lengthComputable) {
        this.onLoading(event.loaded / event.total, id);
      }
    }

    /**
     * Loading event
     * @param percentage
     * @param id
     */
    onLoading(percentage, id) {
      const { files } = this.state;
      this.setState({
        files: [
          ...files.map(file => {
            if (file.id === id) {
              file.percentage = percentage;
            }
            return file;
          }),
        ],
      });
    }

    /**
     * Call upload function
     */
    onStartUpload() {
      this.startUpload(this.state.files);
    }

    /**
     * Upload event
     * @param id
     * @param response
     */
    onUpload(id, response) {
      const { onSuccess } = this.props;

      if (response.status < 200 || response.status >= 300) {
        this.onError(id, response.statusText, response.status);
      } else {
        const file = response.data;
        this.setState({
          files: [
            ...this.state.files.map(item => {
              if (item.id === id) {
                return {
                  ...this.fileAdapter(file),
                  loading: false,
                };
              }
              return item;
            }),
          ],
          uploading: {
            ...this.state.uploading,
            [id]: false,
          },
        });
        this.requests[id] = undefined;
        onSuccess(response);
        this.handleChange(file);
      }
    }

    onError(id, error) {
      const { responseFieldId, errorFormatter } = this.props;

      const uploading = this.state.uploading;
      if (uploading) {
        uploading[id] = false;
      }
      this.setState({
        uploading,
        ...this.state.files.map(file => {
          if (file.id === id) {
            let formattedError = null;

            if (errorFormatter) {
              formattedError = errorFormatter(error);
            } else {
              formattedError = isString(error)
                ? error
                : error[responseFieldId] || status;
            }

            file.error = formattedError;
          }
        }),
      });
    }

    onDragEnter() {
      this.setState({
        uploaderClass: 'n2o-file-uploader-event-drag-enter',
      });
    }

    onDragLeave() {
      this.setState({
        uploaderClass: null,
      });
    }

    render() {
      return <WrappedComponent {...this.getUploaderProps()} />;
    }
  }

  ReturnedComponent.contextTypes = {
    _reduxForm: PropTypes.string,
  };

  ReturnedComponent.defaultProps = {
    label: 'Загрузить файл',
    requestParam: 'file',
    visible: true,
    icon: 'fa fa-upload',
    statusBarColor: 'success',
    multi: true,
    disabled: false,
    autoUpload: true,
    showSize: true,
    value: [],
    onChange: () => {},
    onStart: () => {},
    onSuccess: () => {},
    onDelete: () => {},
  };

  ReturnedComponent.propTypes = {
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    statusFieldId: PropTypes.string,
    sizeFieldId: PropTypes.string,
    responseFieldId: PropTypes.string,
    urlFieldId: PropTypes.string,
    uploadUrl: PropTypes.string,
    deleteUrl: PropTypes.string,
    multi: PropTypes.bool,
    files: PropTypes.arrayOf(PropTypes.object),
    autoUpload: PropTypes.bool,
    maxSize: PropTypes.number,
    minSize: PropTypes.number,
    label: PropTypes.string,
    requestParam: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    icon: PropTypes.string,
    statusBarColor: PropTypes.string,
    saveBtnStyle: PropTypes.object,
    showSize: PropTypes.bool,
    onChange: PropTypes.func,
    className: PropTypes.string,
    mapper: PropTypes.func,
    children: PropTypes.oneOfType([PropTypes.func, PropTypes.node]),
    errorFormatter: PropTypes.func,
  };

  return ReturnedComponent;
};

export default FileUploaderControl;
