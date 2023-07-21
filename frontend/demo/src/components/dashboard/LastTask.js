/**
 * Тест таблицы
 */
import React from 'react';
import { BasicTable } from "n2o-framework/lib/components/Table";
import TextTableHeader from 'n2o-framework/lib/components/widgets/Table/headers/TextTableHeader';
import TextCell from 'n2o-framework/lib/components/widgets/Table/cells/TextCell/TextCell';
import ProgressBarCell from 'n2o-framework/lib/components/widgets/Table/cells/ProgressBarCell/ProgressBarCell';
import IconCell from 'n2o-framework/lib/components/widgets/Table/cells/IconCell/IconCell';

const tableData = [
  {id: "1", task: "Добавить компонент Input", progress: "15", tags: 'N2O', date: "15.03.2018"},
  {id: "2", task: "Загрузить пакет иконок", progress: "65", tags: 'React', date: "12.03.2018"},
  {id: "3", task: "Рефакторинг модели виджетов", progress: "30", tags: 'Java', date: "07.03.2018"},
  {id: "4", task: "Удалить возможность сохранения статуса задачи", progress: "87", tags: 'XML', date: "05.03.2018"},
  {id: "5", task: "Внедрить Bootstrap 4", progress: "51", tags: 'JavaScript', date: "04.03.2018"},
];

const progressColor = {
  "1": "danger",
  "2": "warning",
  "3": "danger",
  "4": "success",
  "5": "warning",
};

class LastTask extends React.Component {
 render() {
   return (
     <BasicTable>
       <BasicTable.Header>
         <BasicTable.Row>
             <BasicTable.HeaderCell>
                <TextTableHeader id="task" label="Задача"/>
             </BasicTable.HeaderCell>
             <BasicTable.HeaderCell component={TextTableHeader} >
                <TextTableHeader id="progress" label="Прогресс"/>
             </BasicTable.HeaderCell>
             <BasicTable.HeaderCell component={TextTableHeader} >
                <TextTableHeader id="tags" label="Теги"/>
             </BasicTable.HeaderCell>
             <BasicTable.HeaderCell component={TextTableHeader} >
                <TextTableHeader id="date" label="Дата"/>
             </BasicTable.HeaderCell>
             <BasicTable.HeaderCell component={TextTableHeader} >
                <TextTableHeader id="actions" label=""/>
             </BasicTable.HeaderCell>
         </BasicTable.Row>
       </BasicTable.Header>
       <BasicTable.Body>
         {
           tableData.map((data, index) => (
             <BasicTable.Row key={index}>
                <BasicTable.Cell id="task">
                    <TextCell model={data} fieldKey="task"/>
                </BasicTable.Cell>
                <BasicTable.Cell id="progress">
                    <ProgressBarCell model={data} color={progressColor[data.id]}/>
                </BasicTable.Cell>
                <BasicTable.Cell id="tags">
                    <span className={`label label-primary`}>{data['tags']}</span>
                </BasicTable.Cell>
                <BasicTable.Cell id="date">
                    <TextCell model={data} fieldKey="date"/>
                </BasicTable.Cell>
                <BasicTable.Cell id="actions">
                    <IconCell model={data} id="actions" icon="fa fa-trash" />
                </BasicTable.Cell>
             </BasicTable.Row>
           ))
         }
       </BasicTable.Body>
     </BasicTable>
   )
 }
}

export default LastTask;
