package com.example.projek_map.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SimpananDao_Impl implements SimpananDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Simpanan> __insertionAdapterOfSimpanan;

  public SimpananDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSimpanan = new EntityInsertionAdapter<Simpanan>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `simpanan` (`kodePegawai`,`simpananPokok`,`simpananWajib`,`simpananSukarela`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Simpanan entity) {
        if (entity.getKodePegawai() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKodePegawai());
        }
        statement.bindDouble(2, entity.getSimpananPokok());
        statement.bindDouble(3, entity.getSimpananWajib());
        statement.bindDouble(4, entity.getSimpananSukarela());
      }
    };
  }

  @Override
  public Object insertAllSimpanan(final List<Simpanan> list,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSimpanan.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSimpanan(final Continuation<? super List<Simpanan>> $completion) {
    final String _sql = "SELECT * FROM simpanan";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Simpanan>>() {
      @Override
      @NonNull
      public List<Simpanan> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfSimpananPokok = CursorUtil.getColumnIndexOrThrow(_cursor, "simpananPokok");
          final int _cursorIndexOfSimpananWajib = CursorUtil.getColumnIndexOrThrow(_cursor, "simpananWajib");
          final int _cursorIndexOfSimpananSukarela = CursorUtil.getColumnIndexOrThrow(_cursor, "simpananSukarela");
          final List<Simpanan> _result = new ArrayList<Simpanan>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Simpanan _item;
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final double _tmpSimpananPokok;
            _tmpSimpananPokok = _cursor.getDouble(_cursorIndexOfSimpananPokok);
            final double _tmpSimpananWajib;
            _tmpSimpananWajib = _cursor.getDouble(_cursorIndexOfSimpananWajib);
            final double _tmpSimpananSukarela;
            _tmpSimpananSukarela = _cursor.getDouble(_cursorIndexOfSimpananSukarela);
            _item = new Simpanan(_tmpKodePegawai,_tmpSimpananPokok,_tmpSimpananWajib,_tmpSimpananSukarela);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSimpananByKode(final String kode,
      final Continuation<? super Simpanan> $completion) {
    final String _sql = "SELECT * FROM simpanan WHERE kodePegawai = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Simpanan>() {
      @Override
      @Nullable
      public Simpanan call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfSimpananPokok = CursorUtil.getColumnIndexOrThrow(_cursor, "simpananPokok");
          final int _cursorIndexOfSimpananWajib = CursorUtil.getColumnIndexOrThrow(_cursor, "simpananWajib");
          final int _cursorIndexOfSimpananSukarela = CursorUtil.getColumnIndexOrThrow(_cursor, "simpananSukarela");
          final Simpanan _result;
          if (_cursor.moveToFirst()) {
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final double _tmpSimpananPokok;
            _tmpSimpananPokok = _cursor.getDouble(_cursorIndexOfSimpananPokok);
            final double _tmpSimpananWajib;
            _tmpSimpananWajib = _cursor.getDouble(_cursorIndexOfSimpananWajib);
            final double _tmpSimpananSukarela;
            _tmpSimpananSukarela = _cursor.getDouble(_cursorIndexOfSimpananSukarela);
            _result = new Simpanan(_tmpKodePegawai,_tmpSimpananPokok,_tmpSimpananWajib,_tmpSimpananSukarela);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
