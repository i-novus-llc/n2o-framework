/**
 * Тест таблицы
 */
import React from 'react';
import Table from "n2o-framework/lib/components/widgets/Table/Table";
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
     <Table>
       <Table.Header>
         <Table.Row>
           <Table.Cell as="th" component={TextTableHeader} id="task" label="Задача" />
           <Table.Cell as="th" component={TextTableHeader} id="progress" label="Прогресс" />
           <Table.Cell as="th" component={TextTableHeader} id="tags" label="Теги" />
           <Table.Cell as="th" component={TextTableHeader} id="date" label="Дата" />
           <Table.Cell as="th" component={TextTableHeader} id="actions" label="" />
         </Table.Row>
       </Table.Header>
       <Table.Body>
         {
           tableData.map((data) => (
             <Table.Row>
               <Table.Cell component={TextCell} model={data} id="task" fieldKey="task" />
               <Table.Cell component={ProgressBarCell} model={data} id="progress" color={progressColor[data.id]} />
               <Table.Cell id="tags">
                 <span className={`label label-primary`}>{data['tags']}</span>
               </Table.Cell>
               <Table.Cell component={TextCell} model={data} id="date" fieldKey="date" />
               <Table.Cell id="actions">
                 <IconCell model={data} id="actions" icon="fa fa-trash" />
               </Table.Cell>
             </Table.Row>
           ))
         }
       </Table.Body>
     </Table>
   )
 }
}

export default LastTask;
