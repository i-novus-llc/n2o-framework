import React from 'react';
import { storiesOf } from '@storybook/react';

import ImageUploaderDropZone from './ImageUploaderDropZone';
import metadata from './ImageUploader.meta.json';
import withForm from 'N2oStorybook/decorators/withForm';
import uniqueId from 'lodash/uniqueId';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

const mockAxios = new MockAdapter(axios, { delayResponse: 500 });
const form = withForm({ src: 'DropZone' });
const stories = storiesOf('Контролы/Загрузчик изображений', module);

stories.add(
  'Upload',
  () => {
    mockAxios.onPost('/n2o/data').reply(function(config) {
      return [
        200,
        {
          customId: `file_${uniqueId()}`,
          customName: config.data.get('avatar').name,
          customSize: config.data.get('avatar').size,
          customStatus: 'success',
          customResponse: 'File uploaded success!',
          customLink: 'https://www.google.com',
        },
      ];
    });

    return <ImageUploaderDropZone {...metadata} />;
  }
  // {
  //   info: {
  //     text: `
  //     Компонент 'Загрузчик файлов'
  //     ~~~js
  //     import ButtonUploader from 'n2o-framework/lib/components/controls/FileUploader/ButtonUploader';
  //
  //     <ButtonUploader
  //         label="Загрузчик файлов"
  //         className="custom-class"
  //         requestParam="avatar"
  //         disabled={false}
  //         uploadUrl="/n2o/data"
  //         deleteUrl="/n2o/data/delete"
  //         autoUpload={true}
  //         showSize={true}
  //         multi={false}
  //         valueFieldId="customId"
  //         labelFieldId="customName"
  //         statusFieldId="customStatus"
  //         sizeFieldId="customSize"
  //         responseFieldId="custonResponse"
  //         urlFieldId="customLink"
  //      />
  //     ~~~
  //     `,
  //   },
  // }
);
