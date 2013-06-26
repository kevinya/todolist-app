package fr.kevinya.todolistapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import fr.kevinya.todolistapp.R;
import fr.kevinya.todolistapp.entity.Task;
import fr.kevinya.todolistapp.service.TaskServiceImpl;

public class AddTaskDialogFragment extends DialogFragment {
	EditText editText;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View returnedView = inflater.inflate(R.layout.dialog_add_task, null);
		editText = (EditText) returnedView.findViewById(R.id.editText1);
		
		builder.setTitle(R.string.add_task)
			.setView(returnedView)
			.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					Task task = new Task();
					task.setName(editText.getText().toString());
					task.setStatus(0);
					task.setVersion(0);
					TaskServiceImpl taskService = TaskServiceImpl.getInstance(getActivity().getBaseContext());
					taskService.getTaskDao().open();
					taskService.create(task);
					taskService.getTaskDao().close();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					AddTaskDialogFragment.this.getDialog().cancel();
				}
			});      
			return builder.create();
	}
}
