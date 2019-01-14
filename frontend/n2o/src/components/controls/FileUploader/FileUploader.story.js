import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import ButtonUploader from './ButtonUploader';
import DropZone from './DropZone';
import mock, { delay } from 'xhr-mock';
import withForm from 'N2oStorybook/decorators/withForm';
import buttonMeta from './ButtonUploader.meta';
import dropzoneMeta from './DropZone.meta';
import { uniqueId } from 'lodash';

const form = withForm({ src: 'DropZone' });
const stories = storiesOf('Контролы/Загрузчик файлов', module);

stories.addDecorator(withKnobs);
mock.setup();
stories
  .add('Single кнопка', () => {
    mock.post(
      '/n2o/data',
      delay(
        {
          status: 201,
          body: JSON.stringify({
            customId: 2,
            customName: 'файл с сервера.png',
            customSize: '83921',
            customStatus: 'success',
            customResponse: 'response',
            customLink: 'google.com'
          })
        },
        500
      )
    );

    return <ButtonUploader {...buttonMeta} />;
  })
  .add('Single DropZone', () => {
    mock.post(
      '/n2o/data',
      delay(
        {
          status: 201,
          body: JSON.stringify({
            customId: 2,
            customName: 'test.png',
            customSize: '83921',
            customStatus: 'success',
            customResponse: 'response',
            customLink: 'google.com'
          })
        },
        500
      )
    );

    return <DropZone {...dropzoneMeta} />;
  })
  .add('Предустановленные значения single режим', () => {
    const props = {
      label: 'Загрузчик файлов',
      className: 'custom-class',
      name: 'avatar',
      disabled: false,
      uploadUrl: '/test',
      showSize: true,
      multi: false,
      valueFieldId: 'id',
      labelFieldId: 'name',
      statusFieldId: 'status',
      sizeFieldId: 'size',
      responseFieldId: 'response',
      urlFieldId: 'link',
      files: [
        {
          id: 1,
          name: 'image.png',
          size: '1202031',
          percentage: 10
        }
      ]
    };

    mock.post(
      '/n2o/data-multi',
      delay(
        {
          status: 201,
          body: JSON.stringify({
            customId: 2,
            customName: 'test.png',
            customSize: '83921',
            customStatus: 'success',
            customResponse: 'response',
            customLink: 'google.com'
          })
        },
        500
      )
    );

    return <ButtonUploader {...props} />;
  })
  .add('Мультизагрузка', () => {
    const props = {
      label: 'Загрузчик файлов',
      className: 'custom-class',
      name: 'avatar',
      disabled: false,
      uploadUrl: '/test',
      autoUpload: true,
      valueFieldId: 'id',
      labelFieldId: 'name',
      statusFieldId: 'status',
      sizeFieldId: 'size',
      responseFieldId: 'response',
      urlFieldId: 'link',
      showSize: true
    };

    return <ButtonUploader {...props} />;
  })
  .add('Мульзагрузка DropZone', () => {
    const props = {
      label: 'Загрузчик файлов',
      className: 'custom-class',
      name: 'avatar',
      disabled: false,
      uploadUrl: '/test',
      autoUpload: false,
      valueFieldId: 'id',
      labelFieldId: 'name',
      statusFieldId: 'status',
      sizeFieldId: 'size',
      responseFieldId: 'response',
      urlFieldId: 'link',
      showSize: true
    };
    return <DropZone {...props} />;
  })
  .add('Мультизагрузка с предустановленными значениями', () => {
    const props = {
      label: 'Загрузчик файлов',
      className: 'custom-class',
      name: 'avatar',
      disabled: false,
      uploadUrl: '/test',
      autoUpload: true,
      showSize: true,
      valueFieldId: 'id',
      labelFieldId: 'name',
      statusFieldId: 'status',
      sizeFieldId: 'size',
      responseFieldId: 'response',
      urlFieldId: 'link',
      files: [
        {
          id: 1,
          name: 'first.jpg',
          size: '231321'
        },
        {
          id: 2,
          name: 'second.jpg',
          size: '897978'
        }
      ]
    };

    return <ButtonUploader {...props} />;
  })
  .add('disabled режим', () => {
    const props = {
      label: 'Загрузчик файлов',
      className: 'custom-class',
      name: 'avatar',
      disabled: false,
      uploadUrl: '/test',
      autoUpload: true,
      showSize: true,
      disabled: true,
      valueFieldId: 'id',
      labelFieldId: 'name',
      statusFieldId: 'status',
      sizeFieldId: 'size',
      responseFieldId: 'response',
      urlFieldId: 'link',
      files: [
        {
          id: 1,
          name: 'first.jpg',
          size: '231321'
        },
        {
          id: 2,
          name: 'second.jpg',
          size: '897978'
        }
      ]
    };

    return <ButtonUploader {...props} />;
  })
  .add(
    'Компонент в форме',
    form(() => {
      mock.post(
        '/n2o/data',
        delay((req, res) => {
          return res.status(201).body(
            JSON.stringify({
              customId: `file_${uniqueId()}`,
              customName: req.body().get('avatar').name,
              customSize: req.body().get('avatar').size,
              customStatus: 'success',
              customResponse: 'File uploaded success!',
              customLink: 'https://www.google.com'
            })
          );
        }, 500)
      );
      return {
        ...dropzoneMeta,
        multi: true,
        autoUpload: true
      };
    })
  );
