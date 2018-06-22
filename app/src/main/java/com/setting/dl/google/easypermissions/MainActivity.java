package com.setting.dl.google.easypermissions;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener, EasyPermissions.PermissionCallbacks {
	
	private final        String TAG         = getClass().getSimpleName();
	private static final int    RC_PERMISSIONS = 2;
	
	private ViewGroup      permissionsLayout;
	private CompoundButton[] permissionsSwitches;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		permissionsLayout = findViewById(R.id.permissionsLayout);
		showPermissions(getPermissions());
	}
	
	private void showPermissions(String[] permissions){
		
		if(permissions == null) return;
		
		if(permissions.length == 0) {
			
			Log.i(TAG, "istenen bir izin yok");
			return;
		}
		
		permissionsSwitches = new CompoundButton[permissions.length];
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 14, 0, 4);
		
		for(int i = 0; i < permissions.length; i++) {
			
			String permission = permissions[i];
			boolean isGranted = EasyPermissions.hasPermissions(this, permission);
			
			
			Switch permissionSwitch = new Switch(this);
			
			permissionSwitch.setLayoutParams(params);
			permissionSwitch.setTextColor(ContextCompat.getColor(this, R.color.colorSwitch));
			permissionSwitch.setTextSize(18f);
			permissionSwitch.setText(permission.substring(permission.lastIndexOf(".") + 1));
			permissionSwitch.setChecked(isGranted);
			permissionSwitch.setTag(permission);
			
			permissionsLayout.addView(permissionSwitch);
			
			permissionSwitch.setOnCheckedChangeListener(this);
			permissionsSwitches[i] = permissionSwitch;
		}
	}
	
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(!isChecked) return;
		
		String permission = buttonView.getTag().toString();
		
		if (EasyPermissions.hasPermissions(this, permission)) {
			
			Toast.makeText(this, "grant " + permission, Toast.LENGTH_LONG).show();
		}
		else{
			
			ActivityCompat.requestPermissions(this, new String[]{permission}, RC_PERMISSIONS);
		}
		
	}
	
	@Override
	public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
		
		checkPermissions();
	}
	
	@Override
	public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
		
		checkPermissions();
	}
	
	
	private void checkPermissions(){
		
		for (CompoundButton _switch : permissionsSwitches) {
			
			String permission = _switch.getTag().toString();
			
			if (EasyPermissions.hasPermissions(this, permission)) {
				
				_switch.setChecked(true);
			}
			else{
				
				_switch.setChecked(false);
			}
			
		}
	}
	
	public String[] getPermissions() {
		
		try {
			
			return getPackageManager()
					.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS)
					.requestedPermissions;
		}
		catch (PackageManager.NameNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
}
















/*private void checkPermissions(){
		
		if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) {
			
			//izin var veya sormaya gerek olmayacak bir api seviyesinde
			contactsFunction();
		}
		else{
			
			//izinlerin sorulması gerek
			EasyPermissions.requestPermissions(this, "izinleri isteme sebebim şudur", RC_CONTACTS, Manifest.permission.READ_CONTACTS);
		}
	}
	
	private void contactsFunction(){
		
		Log.i(TAG, "Rehber işlemleri yapılıyor");
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
	
	
	@AfterPermissionGranted(RC_CONTACTS)
	private void onContactsPermissionsGranted(){
		
		//rehber izinleri tamam
	}
	
	@Override
	public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
		
	}
	
	@Override
	public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
		
		if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
			new AppSettingsDialog.Builder(this).build().show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
			
			Log.i(TAG, "izinleri tekrar kontrol et");
		}
	}*/

















	/*
	
	
	checkContactsPermissions();
	private void checkContactsPermissions(){
		
		if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
			
			Log.i(TAG, "izin yok");
			
			if(shouldRationale(Manifest.permission.READ_CONTACTS)){
				
				Log.i(TAG, "Sebep göstermeye gerek var");
				
				showRationaleForContacts();
			}
			else{
				
				Log.i(TAG, "Sebep göstermeye gerek yok");
				askContactsPermissions();
			}
		}
		else{
			
			Log.i(TAG, "izin var");
		}
	}
	
	private boolean shouldRationale(String permission) {
		
		return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
	}
	
	private void showRationaleForContacts(){
		
		new AlertDialog.Builder(this)
				.setMessage("Burada izinleri neden istediğimizi yazıyoruz. " +
						"İzinleri vermek için TAMAM'a bas, " +
						"izin vermek istemiyorsan IPTAL 'e bas")
				.setPositiveButton("TAMAM", (dialog, button) -> askContactsPermissions())
				.setNegativeButton("IPTAL", (dialog, button) -> {})
				.show();
		
	}
	
	private void askContactsPermissions(){
		
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, RC_CONTACTS);
	}
	
	private void goToAppDetailsActivity() {
		
		final Intent i = new Intent();
		i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		i.setData(Uri.parse("package:" + getPackageName()));
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivityForResult(i, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
			
			Log.i(TAG, "tekrar kontrol et");
		}
	}
	
	private void showSettingsDialog(){
		
		new AlertDialog.Builder(this)
				.setMessage("İzinleri uygulama ayarlarından açabilirsin")
				.setPositiveButton("Ayarlar", (dialog, button) -> goToAppDetailsActivity())
				.setNegativeButton("İptal", (dialog, button) -> {})
				.show();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		
		if (requestCode == RC_CONTACTS) {
			
			String message = String.format("%s : %s", permissions[0], grantResults[0] == PackageManager.PERMISSION_DENIED ? 
					"PERMISSION_DENIED" : "PERMISSION_GRANTED");
			
			
			if(grantResults[0] == PackageManager.PERMISSION_DENIED){
				
				if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])){
					
					Log.i(TAG, "kutucuğa düştük");
					
					showSettingsDialog();
				}
			}
			
			Log.i(TAG, message);
		}
	}
*/
	
	/*
	
	public static boolean hasPermissions(Context context, String... permissions) {
		
		for (String permission : permissions)
			if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) return false;
		
		return true;
	
	}
*/